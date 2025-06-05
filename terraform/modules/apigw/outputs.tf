output "api_endpoint" {
  value = aws_apigatewayv2_api.this.api_endpoint
}

output "defined_application_routes" {
  description = "List of application routes (METHOD /path) exposed via API Gateway."
  value = keys(local.routes)
}
