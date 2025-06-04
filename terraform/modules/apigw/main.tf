locals {
  # Este es el prefijo que tu aplicación tiene configurado con spring.webflux.base-path
  app_base_path_on_alb = "/franchises"

  # Clave: "METODO /ruta/expuesta/por/api/gateway"
  # Valor: "/ruta/completa/en/el/backend/alb (incluyendo app_base_path_on_alb)"
  routes = {
    # Franchise Endpoints
    "POST /api/v1/franchise" = "${local.app_base_path_on_alb}/api/v1/franchise"
    "PATCH /api/v1/franchise/{franchiseUuid}" = "${local.app_base_path_on_alb}/api/v1/franchise/{franchiseUuid}"

    # Branch Endpoints
    "POST /api/v1/branch" = "${local.app_base_path_on_alb}/api/v1/branch"
    "PATCH /api/v1/branch/{branchUuid}" = "${local.app_base_path_on_alb}/api/v1/branch/{branchUuid}"

    # Product Endpoints
    "POST /api/v1/product"                    = "${local.app_base_path_on_alb}/api/v1/product"
    "DELETE /api/v1/product/{productUuid}" = "${local.app_base_path_on_alb}/api/v1/product/{productUuid}"
    # Asumo que tu PATCH para producto también usa productUuid como en DELETE
    "PATCH /api/v1/product/{productUuid}"     = "${local.app_base_path_on_alb}/api/v1/product/{productUuid}"
    "GET /api/v1/product/top/{franchiseUuid}" = "${local.app_base_path_on_alb}/api/v1/product/top/{franchiseUuid}"

    # Health Check (basado en tu arreglo manual que funcionó)
    # Si quieres exponerlo como /health en API Gateway:
    "GET /health" = "${local.app_base_path_on_alb}/actuator/health"
    # O si quieres que sea /franchises/actuator/health también en API Gateway:
    # "GET /franchises/actuator/health" = "${local.app_base_path_on_alb}/actuator/health"
  }
}

resource "aws_cloudwatch_log_group" "apigw_access_logs" {
  # Debes pasar app_name y region como variables a este módulo o definirlas aquí
  name = "/aws/apigateway/${var.app_name}-http-api-access-logs" # Nombre sugerido
  retention_in_days = 1 # Configura la retención como necesites
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
    destination_arn = aws_cloudwatch_log_group.apigw_access_logs.arn
    # Formato JSON detallado para logs. Puedes personalizarlo.
    format = jsonencode({
      requestId               = "$context.requestId",
      sourceIp                = "$context.identity.sourceIp",
      requestTime             = "$context.requestTime",
      httpMethod              = "$context.httpMethod",
      path                    = "$context.path",
      status                  = "$context.status", # Código de estado de la respuesta
      protocol                = "$context.protocol",
      responseLength          = "$context.responseLength",
      integrationRequestId    = "$context.integration.requestId",
      integrationStatus       = "$context.integration.status", # Código de estado de la integración
      integrationLatency      = "$context.integration.latency",
      integrationErrorMessage = "$context.integrationErrorMessage", # ¡Muy útil para errores!
      error                   = "$context.error.message" # Errores del propio API Gateway
    })
  }
}

resource "aws_apigatewayv2_integration" "this" {
  for_each = local.routes

  api_id                 = aws_apigatewayv2_api.this.id
  integration_type       = "HTTP_PROXY"
  integration_method = split(" ", each.key)[0] # Extrae el MÉTODO (GET, POST, etc.)
  integration_uri = "http://${var.alb_dns_name}${each.value}" # Construye la URI del backend
  payload_format_version = "1.0"
  connection_type        = "INTERNET"
}

resource "aws_apigatewayv2_route" "this" {
  for_each = local.routes

  api_id = aws_apigatewayv2_api.this.id
  route_key = each.key # La ruta expuesta por API Gateway
  target = "integrations/${aws_apigatewayv2_integration.this[each.key].id}"
}
