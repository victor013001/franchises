output "alb_sg_id" {
  description = "ID of the ALB's Security Group."
  value       = aws_security_group.alb.id
}

output "ecs_tasks_sg_id" {
  description = "ID of the ECS tasks' Security Group."
  value       = aws_security_group.ecs_tasks.id
}

output "rds_sg_id" {
  description = "ID of the RDS's Security Group."
  value       = aws_security_group.rds.id
}
