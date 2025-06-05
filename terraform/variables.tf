variable "region" {
  description = "AWS region to deploy resources."
  type        = string
  default     = "us-east-1"
}

variable "app_name" {
  description = "Base name for application resources."
  type        = string
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC."
  type        = string
  default     = "10.0.0.0/16"
}

variable "ecr_repository_name" {
  description = "The name of the ECR repository (e.g., franchise-app)."
  type        = string
  default     = "franchise-app"
}

variable "ecr_image_tag" {
  description = "The tag of the ECR image to use (e.g., 'latest', 'v1.0.0', or a git commit SHA)."
  type        = string
  default     = "latest"
}

variable "active_profile" {
  description = "Spring Boot profile to activate (dev, prod, etc.)."
  type        = string
  default     = "dev"
}

variable "db_name" {
  description = "Database name in RDS."
  type        = string
}

variable "db_username" {
  description = "Admin username for RDS."
  type        = string
}

variable "db_password" {
  description = "Password for the RDS admin user."
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "Instance class for RDS."
  type        = string
  default     = "db.t3.micro"
}

variable "db_allocated_storage" {
  description = "Allocated storage for RDS in GB."
  type        = number
  default     = 20
}

variable "db_engine" {
  description = "Database engine (e.g., mysql, postgres)."
  type        = string
  default     = "mysql"
}

variable "db_engine_version" {
  description = "Database engine version."
  type        = string
  default     = "8.0.35"
}

variable "db_port" {
  description = "Port for the RDS instance."
  type        = number
  default     = 3306
}

variable "db_multi_az" {
  description = "Enable Multi-AZ for RDS (not free tier)."
  type        = bool
  default     = false
}

variable "db_backup_retention_period" {
  description = "Backup retention period for RDS in days."
  type        = number
  default     = 1
}

variable "db_max_allocated_storage" {
  description = "Maximum storage RDS can auto-scale to (if enabled)."
  type        = number
  default     = 25
}

// ECS
variable "ecs_cpu" {
  description = "CPU for the ECS Fargate task (units)."
  type        = string
  default     = "256"
}

variable "ecs_memory" {
  description = "Memory for the ECS Fargate task (MiB)."
  type        = string
  default     = "512"
}

variable "ecs_desired_count" {
  description = "Desired number of ECS tasks."
  type        = number
  default     = 1
}

variable "ecs_container_port" {
  description = "Port the application listens on inside the container."
  type        = number
  default     = 8080
}

variable "ecs_log_retention_days" {
  description = "Retention days for ECS task logs."
  type        = number
  default     = 1
}

variable "apigw_log_retention_days" {
  description = "Retention days for API Gateway logs."
  type        = number
  default     = 1
}
