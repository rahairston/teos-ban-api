# teos-ban-api
API for Teos Ban Appeals

## Redis Configuration

Due to the likelihood of people using this (or rather unlikelihood) I am going to be using a redis container or the service as a sidecar in the ec2 instance. This is because the AWS Elasticache instance is way too pricy for something so ideally infrequently used.

## AWS Configurations

The list of AWS Configurations necessary to have a fully functional API

### IAM

- Create a policy/policies with permissions to access the items listed below
    - S3 PUT/GET/DELETE
    - RDS connect

- Create a role with a service type of EC2 and select the policy/ies created above. Should be "AssumeRole"d automatically
- Attach the role to the EC2 instance

This does assume that EC2 will tbe the way to go (given the Java direction this API has taken) however Lambda with a different lamnguage or an EKS cluster (with a K8S service account trust relation ship) could also be options

### Security Groups

- Security groups will be necessary for API to Redis/RDS/Load Balancer connections

### S3

- Standard S3 bucket created for Evidence uploading and storage. Add bucket policy to restrct to the IAM role that the EC2 instance will assume

```json
"Principal": {
    "AWS": "arn:aws:iam::111111111111:role/ROLENAME"
},
```

### RDS

tbd