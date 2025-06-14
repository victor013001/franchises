variable "app_name" {
  type = string
}

variable "image_url" {
  type = string
}

variable "ecs_cpu" {
  type    = string
  default = "256"
}

variable "ecs_memory" {
  type    = string
  default = "512"
}

variable "desired_count" {
  type    = number
  default = 1
}

variable "execution_role_arn" {
  type = string
}

variable "subnet_ids" {
  type = list(string)
}

variable "ecs_tasks_sg_id" {
  type = string
}

variable "target_group_arn" {
  type = string
}

variable "alb_listener" {
  description = "Fake dependency to wait for listener"
  type        = any
}

variable "environment_variables" {
  type = list(object({ name = string, value = string }))
  default = []
}

variable "secrets_variables" {
  type = list(object({
    name      = string
    valueFrom = string
  }))
  default = []
}

variable "container_port" {
  type    = number
  default = 8080
}

variable "region" {
  type = string
}

variable "ecs_log_group_name" {
  type = string
}
