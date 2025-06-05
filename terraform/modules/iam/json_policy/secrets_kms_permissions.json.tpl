{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "secretsmanager:GetSecretValue",
      "Resource": ${jsonencode(secret_arns)}
    },
    {
      "Effect": "Allow",
      "Action": "kms:Decrypt",
      "Resource": ${jsonencode(kms_key_arns)}
    }
  ]
}
