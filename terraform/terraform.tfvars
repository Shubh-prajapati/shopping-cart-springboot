aws_region      = "ap-south-1"

cluster_name    = "shopping-cart-eks"

vpc_cidr        = "10.0.0.0/16"

public_subnet_1 = "10.0.1.0/24"
public_subnet_2 = "10.0.2.0/24"

private_subnet_1 = "10.0.3.0/24"
private_subnet_2 = "10.0.4.0/24"

instance_type = "m7i-flex.large"

desired_nodes = 2
min_nodes     = 2
max_nodes     = 3