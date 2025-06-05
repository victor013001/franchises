locals {
  config_file = jsondecode(file("${path.module}/json_route/app_routes.json"))
  routes = {
    for apigw_route_key, app_suffix_path in local.config_file.api_gateway_to_app_paths :
    apigw_route_key => "${local.config_file.app_base_path_on_alb}${app_suffix_path}"
  }
  access_log_format_object = jsondecode(file("${path.module}/json_log_format/alb_log_format.json"))
}

resource "aws_apigatewayv2_api" "this" {
  name          = "${var.app_name}-api"
  protocol_type = "HTTP"
}

resource "aws_apigatewayv2_stage" "this" {
  api_id      = aws_apigatewayv2_api.this.id
  name        = "$default"
  auto_deploy = true

  access_log_settings {
    destination_arn = var.apigw_log_group_arn
    format = jsonencode(local.access_log_format_object)
  }
}

resource "aws_apigatewayv2_integration" "this" {
  for_each = local.routes

  api_id                 = aws_apigatewayv2_api.this.id
  integration_type       = "HTTP_PROXY"
  integration_method     = split(" ", each.key)[0]
  integration_uri        = "http://${var.alb_dns_name}${each.value}"
  payload_format_version = "1.0"
  connection_type        = "INTERNET"
}

resource "aws_apigatewayv2_route" "this" {
  for_each = local.routes

  api_id    = aws_apigatewayv2_api.this.id
  route_key = each.key
  target    = "integrations/${aws_apigatewayv2_integration.this[each.key].id}"
}
