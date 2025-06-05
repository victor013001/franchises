variable "app_name" {
  type = string
}

variable "ssm_parameter_paths" {
  description = "A list of SSM parameter paths or path prefixes to grant access to (e.g., [\"/my-app/dev/*\", \"/global/config\"])."
  type = list(string)
  default = []
}

variable "secrets_manager_secret_arns" {
  description = "A list of ARNs for Secrets Manager secrets to grant GetSecretValue access to."
  type = list(string)
  default = []
}

variable "kms_key_arns_for_secrets_decryption" {
  description = "A list of ARNs for KMS keys needed to decrypt the specified secrets."
  type = list(string)
  default = []
}

variable "ecr_repository_arns" {
  description = "A list of ARNs for ECR repositories to grant pull access to."
  type = list(string)
  default = []
}
