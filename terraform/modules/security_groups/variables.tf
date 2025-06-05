variable "app_name" {
  type        = string
  description = "Application name for tagging resources."
}

variable "vpc_id" {
  type        = string
  description = "ID of the VPC where Security Groups will be created."
}

variable "container_port" {
  type        = number
  description = "Port on which the ECS container listens."
}

variable "db_port" {
  type        = number
  description = "Port on which the RDS database listens."
}

variable "alb_http_port" {
  type        = number
  description = "HTTP port on which the ALB listens."
}
