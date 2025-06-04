output "alb_dns_name" {
  value = aws_lb.this.dns_name
}

output "target_group_arn" {
  value = aws_lb_target_group.this.arn
}

output "listener" {
  value = aws_lb_listener.this
}

output "listener_arn" {
  value = aws_lb_listener.this.arn
}
