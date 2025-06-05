variable "app_name" {
  type        = string
  description = "Application name for naming log groups."
}

variable "ecs_log_retention_days" {
  type        = number
  description = "Retention days for ECS task logs."
}

variable "apigw_log_retention_days" {
  type        = number
  description = "Retention days for API Gateway access logs."
}
