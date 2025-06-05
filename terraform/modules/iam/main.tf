data "aws_caller_identity" "current" {}
data "aws_region" "current" {}

resource "aws_iam_role" "ecs_task_execution" {
  name = "${var.app_name}-ecs-task-execution-role"

  assume_role_policy = file("${path.module}/json_policy/ecs_task_assume_role_policy.json")

  tags = {
    Name = "${var.app_name}-ecs-task-execution-role"
  }
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy" "ssm_permissions" {
  count = length(var.ssm_parameter_paths) > 0 ? 1 : 0

  name = "${var.app_name}-ssm-access"
  role = aws_iam_role.ecs_task_execution.id
  policy = templatefile("${path.module}/json_policy/ssm_permissions.json.tpl", {
    ssm_arns = [
      for path in var.ssm_parameter_paths :
      "arn:aws:ssm:${data.aws_region.current.name}:${data.aws_caller_identity.current.account_id}:parameter${path}"
    ]
  })
}

resource "aws_iam_role_policy" "secrets_kms_permissions" {
  count = length(var.secrets_manager_secret_arns) > 0 && length(var.kms_key_arns_for_secrets_decryption) > 0 ? 1 : 0

  name = "${var.app_name}-secrets-kms-access"
  role = aws_iam_role.ecs_task_execution.id
  policy = templatefile("${path.module}/json_policy/secrets_kms_permissions.json.tpl", {
    secret_arns  = var.secrets_manager_secret_arns
    kms_key_arns = var.kms_key_arns_for_secrets_decryption
  })
}

resource "aws_iam_role_policy" "ecr_permissions" {
  count = length(var.ecr_repository_arns) > 0 ? 1 : 0

  name = "${var.app_name}-ecr-access"
  role = aws_iam_role.ecs_task_execution.id
  policy = templatefile("${path.module}/json_policy/ecr_permissions.json.tpl", {
    ecr_repo_arns = var.ecr_repository_arns
  })
}
