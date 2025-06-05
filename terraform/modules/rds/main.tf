resource "aws_db_subnet_group" "this" {
  name       = "${var.app_name}-db-subnet-group"
  subnet_ids = var.subnet_ids

  tags = {
    Name = "${var.app_name}-db-subnet-group"
  }
}

resource "aws_db_instance" "this" {
  identifier              = "${var.app_name}-db"
  engine                  = var.engine
  engine_version          = var.engine_version
  instance_class          = var.db_instance_class
  allocated_storage       = var.db_allocated_storage
  db_name                 = var.db_name
  username                = var.db_username
  password                = var.db_password
  port                    = var.port
  db_subnet_group_name    = aws_db_subnet_group.this.name
  vpc_security_group_ids = [var.rds_sg_id]
  skip_final_snapshot     = true
  publicly_accessible     = false
  storage_encrypted       = false
  deletion_protection     = false
  multi_az                = var.multi_az
  backup_retention_period = var.backup_retention_period
  max_allocated_storage   = var.max_allocated_storage

  tags = {
    Name = "${var.app_name}-db"
  }
}
