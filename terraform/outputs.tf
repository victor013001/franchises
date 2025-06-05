output "apigw_url" {
  description = "API Gateway URL."
  value       = module.apigw.api_endpoint
}

output "application_api_endpoints_exposed" {
  description = "List of application API endpoints (METHOD /path) exposed through API Gateway."
  value       = module.apigw.defined_application_routes
}
