resource "aws_cloudwatch_log_group" "ecs_task_logs" {
  name              = "/ecs/${var.app_name}"
  retention_in_days = var.ecs_log_retention_days
  tags = {
    Name = "${var.app_name}-ecs-task-logs"
  }
}

resource "aws_cloudwatch_log_group" "apigw_access_logs" {
  name              = "/aws/apigateway/${var.app_name}-http-api"
  retention_in_days = var.apigw_log_retention_days
  tags = {
    Name = "${var.app_name}-apigw-access-logs"
  }
}
