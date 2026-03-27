#!/bin/bash

# 🚀 Create AWS S3 Bucket for RevPlay Deployment

echo "🚀 Creating AWS S3 Bucket for RevPlay"
echo "====================================="

# 📋 Configuration
BUCKET_NAME="revplay-music-platform"
REGION="us-east-1"

echo "📋 Configuration:"
echo "   Bucket: $BUCKET_NAME"
echo "   Region: $REGION"

# 🗑️ Create S3 Bucket
echo ""
echo "🗑️ Creating S3 bucket..."
aws s3api create-bucket \
    --bucket $BUCKET_NAME \
    --region $REGION

# 🔐 Disable Public Access Block (for static website)
echo ""
echo "🔐 Configuring bucket permissions..."
aws s3api put-public-access-block \
    --bucket $BUCKET_NAME \
    --public-access-block-configuration "BlockPublicAcls=false,IgnorePublicAcls=false,BlockPublicPolicy=false,RestrictPublicBuckets=false"

# 📋 Add Bucket Policy for Public Access
echo ""
echo "📋 Adding bucket policy..."
cat > bucket-policy.json << EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::$BUCKET_NAME/*"
        }
    ]
}
EOF

aws s3api put-bucket-policy \
    --bucket $BUCKET_NAME \
    --policy file://bucket-policy.json

# 🌐 Enable Static Website Hosting
echo ""
echo "🌐 Enabling static website hosting..."
aws s3 website $BUCKET_NAME \
    --index-document index.html \
    --error-document index.html

# 🗑️ Clean up
rm bucket-policy.json

echo ""
echo "✅ AWS S3 bucket created successfully!"
echo "📋 Bucket URL: http://$BUCKET_NAME.s3-website-$REGION.amazonaws.com"
echo "📋 Direct URL: http://$BUCKET_NAME.s3.amazonaws.com"
echo ""
echo "🔧 Next: Add AWS credentials to GitHub Secrets"
