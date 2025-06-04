variable "region" {
  type    = string
  default = "us-east-1"
}

variable "active_profile" {
  type    = string
  default = "dev"
}

variable "app_name" {
  type    = string
  default = "franchises-app-1"
}

variable "ecr_image_url" {
  type    = string
  default = "512160136338.dkr.ecr.us-east-1.amazonaws.com/franchise-app:latest"
}

variable "db_type" {
  type    = string
  default = "mysql"
}

variable "db_username" {
  description = "Admin username for the RDS database."
  type        = string
  default     = "dbadmin"
}

variable "db_password" {
  description = "Admin password for the RDS database."
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "RDS instance class."
  type        = string
  default     = "db.t3.micro"
}

variable "db_allocated_storage" {
  description = "Allocated storage for RDS in GB."
  type        = number
  default     = 20
}

variable "ecs_cpu" {
  description = "CPU for the ECS Fargate task."
  type        = string
  default     = "256"
}

variable "ecs_memory" {
  description = "Memory for the ECS Fargate task in MiB."
  type        = string
  default     = "512"
}

variable "public_subnets" {
  type = list(string)
  default = [
    "10.0.1.0/24",
    "10.0.2.0/24"
  ]
}

variable "private_subnets" {
  type = list(string)
  default = [
    "10.0.3.0/24",
    "10.0.4.0/24"
  ]
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "db_name" {
  type    = string
  default = "franchisesdb"
}
