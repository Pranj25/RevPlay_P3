# 🚀 RevPlay Microservices Deployment Guide

## 📋 Prerequisites

### **System Requirements**
- **Java 17+** (JDK installed)
- **MySQL 8.0+** 
- **Node.js 16+** (for Angular frontend)
- **Maven 3.8+**
- **Git**

### **Port Configuration**
Ensure these ports are available:
- **8888** - Config Server
- **8761** - Eureka Server  
- **8085** - User Service
- **8082** - Catalog Service
- **8086** - Playlist Service
- **8087** - Favourite Service
- **8088** - Playback Service
- **8089** - Analytics Service
- **9080** - API Gateway
- **4200** - Angular Frontend

---

## 🗄️ Database Setup

### **1. Create MySQL Databases**
Execute these SQL scripts in order:

```bash
# Navigate to database directory
cd "c:\Users\gaytr\OneDrive\Desktop\revplay microservices\database"

# Execute all database schemas
mysql -u root -p < 01_user_service_schema.sql
mysql -u root -p < 02_catalog_service_schema.sql  
mysql -u root -p < 03_playlist_service_schema.sql
mysql -u root -p < 04_playback_service_schema.sql
mysql -u root -p < 05_favourite_service_schema.sql
mysql -u root -p < 06_analytics_service_schema.sql
```

### **2. Verify Database Creation**
```sql
-- Check all databases
SHOW DATABASES LIKE 'revplay_%';

-- Should show:
-- revplay_user_service
-- revplay_catalog_service  
-- revplay_playlist_service
-- revplay_playback_service
-- revplay_favourite_service
-- revplay_analytics_service
```

---

## 🔧 Configuration Setup

### **1. Update Database Credentials**
Update MySQL password in all `application.yml` files:
- **Location**: Each service's `src/main/resources/application.yml`
- **Current password**: `PranjalP@2003`
- **Update to**: Your MySQL root password

### **2. Verify Configuration**
Check these key configurations:

#### **API Gateway** (`api-gateway/src/main/resources/application.yml`)
```yaml
spring:
  application:
    name: api-gateway
  cloud:
    config:
      import: configserver:http://localhost:8888
```

#### **Service URLs**
All services should point to:
- **Config Server**: `http://localhost:8888`
- **Eureka Server**: `http://localhost:8761/eureka/`

---

## 🚀 Deployment Steps

### **Option 1: Automated Startup (Recommended)**

```bash
# Navigate to project root
cd "c:\Users\gaytr\OneDrive\Desktop\revplay microservices"

# Start all services automatically
start-all-services.bat
```

### **Option 2: Manual Startup**

#### **Step 1: Start Infrastructure Services**
```bash
# Config Server (Port 8888)
cd config-server
mvn spring-boot:run

# Eureka Server (Port 8761) 
cd ../eureka-server
mvn spring-boot:run
```

#### **Step 2: Start Business Services**
```bash
# User Service (Port 8085)
cd ../user-service
mvn spring-boot:run

# Catalog Service (Port 8082)
cd ../catalog-service  
mvn spring-boot:run

# API Gateway (Port 9080)
cd ../api-gateway
mvn spring-boot:run

# Playlist Service (Port 8086)
cd ../playlist-service
mvn spring-boot:run

# Favourite Service (Port 8087)
cd ../favourite-service
mvn spring-boot:run

# Playback Service (Port 8088)
cd ../playback-service
mvn spring-boot:run

# Analytics Service (Port 8089)
cd ../analytics-service
mvn spring-boot:run
```

#### **Step 3: Start Frontend**
```bash
# Angular Frontend (Port 4200)
cd ../revplay-frontend
ng serve
```

---

## ✅ Verification Checklist

### **1. Service Registration**
- **Eureka Dashboard**: http://localhost:8761
- **Expected**: All 6 services registered
- **Services**: user-service, catalog-service, playlist-service, favourite-service, playback-service, analytics-service

### **2. API Gateway Health**
- **Gateway URL**: http://localhost:9080
- **Actuator**: http://localhost:9080/actuator/health
- **Expected**: `{"status":"UP"}`

### **3. Individual Service Health**
```bash
# Test each service
curl http://localhost:9080/api/users/actuator/health
curl http://localhost:9080/api/catalog/actuator/health  
curl http://localhost:9080/api/playlist/actuator/health
curl http://localhost:9080/api/favourite/actuator/health
curl http://localhost:9080/api/playback/actuator/health
curl http://localhost:9080/api/analytics/actuator/health
```

### **4. Frontend Access**
- **Application**: http://localhost:4200
- **Expected**: RevPlay Music Streaming Platform

### **5. API Documentation**
- **User Service**: http://localhost:8085/swagger-ui.html
- **Catalog Service**: http://localhost:8082/swagger-ui.html
- **Other Services**: Available at respective ports + `/swagger-ui.html`

---

## 🧪 Testing the Application

### **1. User Registration & Login**
```bash
# Register new user
curl -X POST http://localhost:9080/api/users/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123","firstName":"Test","lastName":"User"}'

# Login
curl -X POST http://localhost:9080/api/users/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

### **2. Music Catalog**
```bash
# Get all songs
curl http://localhost:9080/api/catalog/songs

# Search songs
curl "http://localhost:9080/api/catalog/songs/search?q=test"
```

### **3. Playlist Management**
```bash
# Create playlist (requires auth token)
curl -X POST http://localhost:9080/api/playlists \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"My Playlist","description":"Test playlist","isPublic":false}'
```

---

## 🔧 Troubleshooting

### **Common Issues & Solutions**

#### **1. Service Registration Failed**
- **Cause**: Config Server not running
- **Solution**: Start Config Server first, then restart services

#### **2. Database Connection Errors**
- **Cause**: Wrong database credentials or database not created
- **Solution**: Verify MySQL password and run database scripts

#### **3. Port Conflicts**
- **Cause**: Ports already in use
- **Solution**: Kill processes or change ports in application.yml

#### **4. Frontend Not Loading**
- **Cause**: Backend services not running
- **Solution**: Verify all microservices are started

#### **5. CORS Errors**
- **Cause**: Frontend trying to access services directly
- **Solution**: Use API Gateway (port 9080) for all frontend requests

### **Useful Commands**

```bash
# Check running Java processes
jps -l

# Kill all Java processes
taskkill /F /IM java.exe

# Check port usage  
netstat -ano | findstr :9080

# Restart all services
stop-all-services.bat
start-all-services.bat
```

---

## 🎯 Production Deployment

### **Environment Variables**
For production, set these environment variables:
```bash
# Database
MYSQL_HOST=your-db-host
MYSQL_USER=your-db-user  
MYSQL_PASSWORD=your-db-password

# Security
JWT_SECRET=your-super-secret-jwt-key
JWT_EXPIRATION=86400000

# URLs
CONFIG_SERVER_URL=http://config-server:8888
EUREKA_SERVER_URL=http://eureka-server:8761
```

### **Docker Deployment (Optional)**
```bash
# Build Docker images
mvn clean package spring-boot:build-image

# Run with Docker Compose
docker-compose up -d
```

### **Cloud Deployment**
- **AWS**: Use ECS/EKS with RDS
- **Azure**: Use Container Instances with Azure SQL  
- **GCP**: Use Cloud Run with Cloud SQL
- **DigitalOcean**: Use App Platform with Managed Databases

---

## 📊 Monitoring & Logging

### **Health Checks**
All services expose `/actuator/health` endpoint

### **Metrics**
- **Prometheus**: `/actuator/prometheus`
- **Custom Metrics**: Available via Analytics Service

### **Logging**
- **Level**: DEBUG for development, INFO for production
- **Location**: Console output (configure file logging for production)

---

## 🎉 Success Criteria

✅ **Deployment Complete When:**
- All 6 microservices registered in Eureka
- API Gateway responding to requests  
- Frontend loads and authenticates users
- Database connections established
- Basic CRUD operations working
- Music streaming functional

---

## 🆘 Support

### **Getting Help**
1. Check logs in individual service terminals
2. Verify Eureka Dashboard registration
3. Test API Gateway health endpoint
4. Review this troubleshooting guide

### **Next Steps**
1. **Add Monitoring**: Prometheus + Grafana
2. **Add CI/CD**: GitHub Actions/Jenkins
3. **Add Security**: OAuth2, SSL certificates
4. **Scale Up**: Load balancers, multiple instances

---

**🎊 Congratulations! Your RevPlay microservices platform is now deployed and ready for production!**

## 📞 Quick Reference

| Service | Port | URL | Health Check |
|---------|------|-----|--------------|
| Config Server | 8888 | http://localhost:8888 | /actuator/health |
| Eureka Server | 8761 | http://localhost:8761 | /actuator/health |
| API Gateway | 9080 | http://localhost:9080 | /actuator/health |
| User Service | 8085 | http://localhost:8085 | /actuator/health |
| Catalog Service | 8082 | http://localhost:8082 | /actuator/health |
| Playlist Service | 8086 | http://localhost:8086 | /actuator/health |
| Favourite Service | 8087 | http://localhost:8087 | /actuator/health |
| Playback Service | 8088 | http://localhost:8088 | /actuator/health |
| Analytics Service | 8089 | http://localhost:8089 | /actuator/health |
| Frontend | 4200 | http://localhost:4200 | - |

**🚀 Happy Streaming!**
