resource "aws_ecs_cluster" "this" {
  name = "${var.app_name}-cluster"
}

resource "aws_cloudwatch_log_group" "task_logs" {
  name = "/ecs/${var.app_name}" # El mismo nombre que esperas usar
  retention_in_days = var.log_retention_days

  tags = {
    Name        = "${var.app_name}-ecs-task-logs"
    Application = var.app_name
  }
}

resource "aws_ecs_task_definition" "this" {
  family             = "${var.app_name}-task"
  cpu                = var.ecs_cpu
  memory             = var.ecs_memory
  network_mode       = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn = var.execution_role_arn

  container_definitions = jsonencode([
    {
      name  = var.app_name
      image = var.image_url
      portMappings = [
        {
          containerPort = var.container_port
          protocol      = "tcp"
        }
      ],
      environment = var.environment_variables,
      secrets     = var.secrets_variables,
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          # Usar el nombre del grupo de logs creado explícitamente
          "awslogs-group"         = aws_cloudwatch_log_group.task_logs.name,
          "awslogs-region"        = var.region, # Asegúrate que esta variable esté correcta
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "this" {
  name            = "${var.app_name}-service"
  cluster         = aws_ecs_cluster.this.id
  task_definition = aws_ecs_task_definition.this.arn
  launch_type     = "FARGATE"
  desired_count   = var.desired_count

  network_configuration {
    subnets          = var.subnet_ids
    security_groups = [var.security_group_id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = var.target_group_arn
    container_name   = var.app_name
    container_port   = var.container_port
  }

  depends_on = [var.alb_listener]
}
