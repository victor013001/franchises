resource "aws_secretsmanager_secret" "this" {
  name        = "${var.app_name}-rds-credentials"
  description = "RDS credentials for ${var.app_name}"

  lifecycle {
    ignore_changes = all
  }
}

resource "aws_secretsmanager_secret_version" "this" {
  secret_id = aws_secretsmanager_secret.this.id
  secret_string = jsonencode({
    username = var.username
    password = var.password
  })

  lifecycle {
    ignore_changes = all
  }
}
