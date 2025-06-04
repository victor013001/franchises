module "iam" {
  source   = "./modules/iam"
  app_name = var.app_name
}

module "secret_manager" {
  source   = "./modules/secret_manager"
  app_name = var.app_name
  username = var.db_username
  password = var.db_password
}


module "rds" {
  source               = "./modules/rds"
  app_name             = var.app_name
  db_username          = var.db_username
  db_password          = var.db_password
  db_name              = var.db_name
  db_instance_class    = var.db_instance_class
  db_allocated_storage = var.db_allocated_storage
  security_group_ids = [module.network.app_sg_id]
  subnet_ids           = module.network.private_subnets
}

module "network" {
  source     = "./modules/network"
  app_name   = var.app_name
  cidr_block = var.vpc_cidr
}

module "alb" {
  source            = "./modules/alb"
  app_name          = var.app_name
  vpc_id            = module.network.vpc_id
  public_subnet_ids = module.network.public_subnets
  security_group_ids = [module.network.app_sg_id]
}

module "apigw" {
  source       = "./modules/apigw"
  app_name     = var.app_name
  alb_dns_name = module.alb.alb_dns_name
}

module "ecs" {
  source    = "./modules/ecs"
  app_name  = var.app_name
  image_url = var.ecr_image_url
  environment_variables = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.active_profile },
    { name = "DB", value = var.db_type },
    { name = "DB_URL", value = module.rds.db_endpoint },
    { name = "DB_PORT", value = module.rds.db_port },
    { name = "DB_SCHEMA", value = var.db_name }
  ]
  secrets_variables = [
    { name = "DB_USERNAME", valueFrom = "${module.secret_manager.secret_arn}:username::" },
    { name = "DB_PASSWORD", valueFrom = "${module.secret_manager.secret_arn}:password::" }
  ]
  alb_listener       = module.alb.listener_arn
  execution_role_arn = module.iam.ecs_task_execution_role_arn
  security_group_id  = module.network.app_sg_id
  subnet_ids         = module.network.private_subnets
  target_group_arn   = module.alb.target_group_arn
}
