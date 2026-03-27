# 🚀 RevPlay CI/CD Deployment Guide

## 📋 Overview

This guide covers the complete CI/CD pipeline for deploying the RevPlay music streaming frontend using Git workflows.

## 🏗️ Architecture

```
📁 Repository Structure
├── .github/workflows/
│   ├── ci-cd.yml          # Main CI/CD pipeline
│   └── deploy.yml         # Deployment workflow
├── Dockerfile             # Container configuration
├── docker-compose.yml     # Local development
├── nginx.conf            # Web server configuration
├── .lighthouserc.js      # Performance testing
└── src/environments/     # Environment configs
    ├── environment.ts
    ├── environment.staging.ts
    └── environment.prod.ts
```

## 🔄 CI/CD Pipeline

### **🔍 Continuous Integration**

**Triggers:**
- Push to `main` or `develop` branches
- Pull requests to `main`
- Manual workflow dispatch

**Jobs:**
1. **Test** - Linting, unit tests, coverage
2. **Build** - Multi-environment builds
3. **Security** - Security audit and scanning
4. **Performance** - Lighthouse testing
5. **Docker** - Container build and push

### **🚀 Continuous Deployment**

**Environments:**
- **Staging** - Auto-deploy from `develop` branch
- **Production** - Auto-deploy from `main` branch
- **Manual** - Workflow dispatch for controlled deployments

---

## 🔧 Setup Instructions

### **1. Repository Configuration**

```bash
# Initialize Git repository
git init
git add .
git commit -m "🎉 Initial commit: RevPlay music streaming platform"

# Add remote repository
git remote add origin https://github.com/your-username/revplay-frontend.git

# Push to main branch
git push -u origin main
```

### **2. GitHub Secrets**

Configure these secrets in your GitHub repository:

```bash
# 🔐 Required Secrets
DOCKER_USERNAME=your-docker-username
DOCKER_PASSWORD=your-docker-password
AWS_ACCESS_KEY_ID=your-aws-access-key
AWS_SECRET_ACCESS_KEY=your-aws-secret-key
CLOUDFRONT_STAGING_ID=your-staging-cloudfront-id
CLOUDFRONT_PRODUCTION_ID=your-production-cloudfront-id
FIREBASE_TOKEN=your-firebase-token
VERCEL_TOKEN=your-vercel-token
NETLIFY_AUTH_TOKEN=your-netlify-token
SLACK_WEBHOOK_URL=your-slack-webhook
LHCI_GITHUB_APP_TOKEN=your-lighthouse-token
SNYK_TOKEN=your-snyk-token
```

### **3. Branch Strategy**

```bash
# Create development branch
git checkout -b develop
git push origin develop

# Feature branches
git checkout -b feature/new-feature
git push origin feature/new-feature

# Pull request to develop
# Merge to develop triggers staging deployment

# Release to main
git checkout main
git merge develop
git push origin main
# Triggers production deployment
```

---

## 🐳 Docker Deployment

### **1. Build Docker Image**

```bash
# Build locally
docker build -t revplay-frontend .

# Run locally
docker run -p 80:80 revplay-frontend
```

### **2. Docker Compose**

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### **3. Production Docker Deployment**

```bash
# Pull from registry
docker pull revplay/frontend:latest

# Run with environment variables
docker run -d \
  --name revplay-frontend \
  -p 80:80 \
  -e NODE_ENV=production \
  -e API_URL=https://api.revplay.com \
  revplay/frontend:latest
```

---

## 🚀 Deployment Options

### **Option 1: AWS S3 + CloudFront**

```yaml
# GitHub Actions deployment
- name: 🚀 Deploy to AWS
  run: |
    aws s3 sync dist/ s3://www.revplay.com --delete
    aws cloudfront create-invalidation --distribution-id ${{ secrets.CLOUDFRONT_ID }} --paths "/*"
```

### **Option 2: Firebase Hosting**

```yaml
# GitHub Actions deployment
- name: 🚀 Deploy to Firebase
  run: |
    firebase deploy --only hosting:production --token ${{ secrets.FIREBASE_TOKEN }}
```

### **Option 3: Vercel**

```yaml
# GitHub Actions deployment
- name: 🚀 Deploy to Vercel
  run: |
    vercel --prod --token ${{ secrets.VERCEL_TOKEN }}
```

### **Option 4: Netlify**

```yaml
# GitHub Actions deployment
- name: 🚀 Deploy to Netlify
  run: |
    netlify deploy --prod --dir=dist --auth-token ${{ secrets.NETLIFY_AUTH_TOKEN }}
```

### **Option 5: Custom Server**

```yaml
# GitHub Actions deployment
- name: 🚀 Deploy to Custom Server
  run: |
    scp -r dist/* user@server:/var/www/revplay/
    ssh user@server "systemctl reload nginx"
```

---

## 🧪 Testing Pipeline

### **1. Unit Tests**

```bash
# Run tests with coverage
npm run test:ci

# Generate coverage report
npm run test:ci -- --code-coverage
```

### **2. E2E Tests**

```bash
# Run E2E tests
npm run test:e2e

# Run on staging
npm run test:e2e:staging

# Run on production
npm run test:e2e:production
```

### **3. Performance Tests**

```bash
# Run Lighthouse CI
lhci autorun

# Bundle analysis
npm run analyze
```

---

## 📊 Monitoring

### **1. Performance Monitoring**

- **Lighthouse CI** - Automated performance testing
- **Bundle Analysis** - Webpack bundle analyzer
- **Core Web Vitals** - Performance metrics

### **2. Error Monitoring**

- **Sentry** - Error tracking
- **LogRocket** - Session replay
- **Custom logging** - Application logs

### **3. Uptime Monitoring**

- **Pingdom** - Uptime monitoring
- **Status page** - Service status
- **Health checks** - Application health

---

## 🔄 Rollback Procedure

### **1. Automatic Rollback**

```yaml
# GitHub Actions rollback
- name: 🔄 Rollback Deployment
  run: |
    aws s3 sync s3://www.revplay.com.backup s3://www.revplay.com --delete
    aws cloudfront create-invalidation --distribution-id ${{ secrets.CLOUDFRONT_ID }} --paths "/*"
```

### **2. Manual Rollback**

```bash
# Git rollback
git revert <commit-hash>
git push origin main

# Docker rollback
docker run -d --name revplay-rollback revplay/frontend:previous-version

# Database rollback (if needed)
# Rollback database migrations
```

---

## 🔧 Environment Configuration

### **Development Environment**

```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9080',
  debug: true
};
```

### **Staging Environment**

```typescript
// src/environments/environment.staging.ts
export const environment = {
  production: false,
  apiUrl: 'https://staging-api.revplay.com',
  debug: true
};
```

### **Production Environment**

```typescript
// src/environments/environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://api.revplay.com',
  debug: false
};
```

---

## 🚀 Quick Start

### **1. Local Development**

```bash
# Clone repository
git clone https://github.com/your-username/revplay-frontend.git
cd revplay-frontend

# Install dependencies
npm install

# Start development server
npm start
```

### **2. Docker Development**

```bash
# Build and run with Docker Compose
docker-compose up -d

# Access application
# Frontend: http://localhost:80
# Backend: http://localhost:9080
```

### **3. Deploy to Production**

```bash
# Push to main branch
git checkout main
git add .
git commit -m "🚀 Deploy to production"
git push origin main

# Or manual deployment
npm run build:prod
# Deploy dist/ folder to your hosting provider
```

---

## 📋 Best Practices

### **✅ Do's:**
- ✅ Use semantic versioning
- ✅ Write meaningful commit messages
- ✅ Test before deploying
- ✅ Monitor performance
- ✅ Use environment-specific configs
- ✅ Implement rollback procedures
- ✅ Use feature flags
- ✅ Document deployments

### **❌ Don'ts:**
- ❌ Deploy directly to production
- ❌ Skip testing
- ❌ Hardcode secrets
- ❌ Ignore performance metrics
- ❌ Skip rollback planning
- ❌ Deploy without monitoring

---

## 🎯 Success Metrics

### **📊 Deployment Metrics:**
- ✅ **Deployment Time** - < 5 minutes
- ✅ **Uptime** - > 99.9%
- ✅ **Performance Score** - > 90
- ✅ **Error Rate** - < 1%
- ✅ **Test Coverage** - > 80%

### **🚀 CI/CD Metrics:**
- ✅ **Build Time** - < 3 minutes
- ✅ **Test Time** - < 2 minutes
- ✅ **Deploy Time** - < 5 minutes
- ✅ **Rollback Time** - < 2 minutes

---

## 🎉 Conclusion

Your RevPlay frontend is now fully configured with a comprehensive CI/CD pipeline! 

**🚀 Next Steps:**
1. Configure GitHub secrets
2. Set up hosting provider
3. Test deployment pipeline
4. Monitor performance
5. Deploy to production

**🎯 Your application is ready for automated deployments!**
