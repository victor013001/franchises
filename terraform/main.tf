data "aws_caller_identity" "current" {
}

locals {
  dynamic_image_url = "${data.aws_caller_identity.current.account_id}.dkr.ecr.${var.region}.amazonaws.com/${var.ecr_repository_name}:${var.ecr_image_tag}"
}

module "logging" {
  source                   = "./modules/cloudwatch"
  app_name                 = var.app_name
  ecs_log_retention_days   = var.ecs_log_retention_days
  apigw_log_retention_days = var.apigw_log_retention_days
}

module "network" {
  source     = "./modules/network"
  app_name   = var.app_name
  cidr_block = var.vpc_cidr
}

module "security_groups" {
  source         = "./modules/security_groups"
  app_name       = var.app_name
  vpc_id         = module.network.vpc_id
  container_port = var.ecs_container_port
  db_port        = var.db_port
  alb_http_port  = 80
}

module "iam" {
  source   = "./modules/iam"
  app_name = var.app_name
  secrets_manager_secret_arns = [
    module.secret_manager.secret_arn
  ]
  kms_key_arns_for_secrets_decryption = [
    "arn:aws:kms:${var.region}:${data.aws_caller_identity.current.account_id}:alias/aws/secretsmanager"
  ]
  ecr_repository_arns = [
    "arn:aws:ecr:${var.region}:${data.aws_caller_identity.current.account_id}:repository/${var.ecr_repository_name}"
  ]
  ssm_parameter_paths = []
}

module "secret_manager" {
  source   = "./modules/secret_manager"
  app_name = var.app_name
  username = var.db_username
  password = var.db_password
}

module "rds" {
  source                  = "./modules/rds"
  app_name                = var.app_name
  db_username             = var.db_username
  db_password             = var.db_password
  db_name                 = var.db_name
  db_instance_class       = var.db_instance_class
  db_allocated_storage    = var.db_allocated_storage
  subnet_ids              = module.network.private_subnets
  rds_sg_id               = module.security_groups.rds_sg_id
  engine                  = var.db_engine
  engine_version          = var.db_engine_version
  port                    = var.db_port
  multi_az                = var.db_multi_az
  backup_retention_period = var.db_backup_retention_period
  max_allocated_storage   = var.db_max_allocated_storage
}

module "alb" {
  source            = "./modules/alb"
  app_name          = var.app_name
  vpc_id            = module.network.vpc_id
  public_subnet_ids = module.network.public_subnets
  alb_sg_id         = module.security_groups.alb_sg_id
  ecs_target_port   = var.ecs_container_port
}

module "ecs" {
  source             = "./modules/ecs"
  app_name           = var.app_name
  region             = var.region
  image_url          = local.dynamic_image_url
  ecs_cpu            = var.ecs_cpu
  ecs_memory         = var.ecs_memory
  desired_count      = var.ecs_desired_count
  container_port     = var.ecs_container_port
  execution_role_arn = module.iam.ecs_task_execution_role_arn
  subnet_ids         = module.network.private_subnets
  ecs_tasks_sg_id    = module.security_groups.ecs_tasks_sg_id
  target_group_arn   = module.alb.target_group_arn
  alb_listener       = module.alb.listener_arn
  ecs_log_group_name = module.logging.ecs_log_group_name
  environment_variables = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.active_profile },
    { name = "DB", value = var.db_engine },
    { name = "DB_URL", value = module.rds.db_endpoint },
    { name = "DB_PORT", value = tostring(module.rds.db_port) },
    { name = "DB_SCHEMA", value = var.db_name }
  ]
  secrets_variables = [
    { name = "DB_USERNAME", valueFrom = "${module.secret_manager.secret_arn}:username::" },
    { name = "DB_PASSWORD", valueFrom = "${module.secret_manager.secret_arn}:password::" }
  ]
}

module "apigw" {
  source              = "./modules/apigw"
  app_name            = var.app_name
  alb_dns_name        = module.alb.alb_dns_name
  apigw_log_group_arn = module.logging.apigw_log_group_arn
}
