output "ecs_log_group_name" {
  value = aws_cloudwatch_log_group.ecs_task_logs.name
}

output "apigw_log_group_arn" {
  value = aws_cloudwatch_log_group.apigw_access_logs.arn
}
