# 🚀 RevPlay Microservices Management

## 📋 **Service Management Scripts**

### **🎯 Quick Commands (`quick-services.bat`)**
```bash
# Start all services
quick-services.bat start

# Stop all services
quick-services.bat stop

# Check service status
quick-services.bat status

# Build all services
quick-services.bat build
```

### **🎮 Interactive Menu (`manage-services.bat`)**
```bash
# Launch interactive menu system
manage-services.bat
```

**Menu Options:**
1. **Start All Services** - Sequential startup with delays
2. **Stop All Services** - Terminate all Java processes
3. **Check Service Status** - Health check for all services
4. **Build All Services** - Compile all services with error handling
5. **Start Individual Service** - Select specific service to start
6. **Stop Individual Service** - Select specific service to stop
7. **Exit** - Close management tool

---

## 🌐 **Service Endpoints**

### **📡 Service Ports & URLs**
| Service | Port | URL | Health Check |
|----------|-------|------|--------------|
| Config Server | 8888 | http://localhost:8888/actuator/health |
| Eureka Server | 8761 | http://localhost:8761/actuator/health |
| API Gateway | 9080 | http://localhost:9080/actuator/health |
| User Service | 8081 | http://localhost:8081/actuator/health |
| Catalog Service | 8082 | http://localhost:8082/actuator/health |
| Playlist Service | 8083 | http://localhost:8083/actuator/health |
| Favourite Service | 8087 | http://localhost:8087/actuator/health |
| Playback Service | 8085 | http://localhost:8085/actuator/health |
| Analytics Service | 8086 | http://localhost:8086/actuator/health |

---

## 🔧 **Build Process**

### **📦 Build Order & Dependencies**
1. **Common Module** - Built first with `mvn clean install`
2. **Config Server** - Configuration service
3. **Eureka Server** - Service discovery
4. **API Gateway** - Updated with common dependency
5. **User Service** - Authentication & user management
6. **Catalog Service** - Music catalog management
7. **Playlist Service** - Playlist operations
8. **Favourite Service** - Likes/favorites management
9. **Playback Service** - Music playback
10. **Analytics Service** - Usage analytics

### **✅ Build Verification**
- **Error Handling**: Stops on first failure
- **Success Messages**: Confirms each successful build
- **Dependency Resolution**: Common module built first
- **Compilation Check**: Maven compile verification

---

## 🏥 **Service Management**

### **🚀 Starting Services**
```bash
# Quick start (recommended for development)
quick-services.bat start

# Interactive start (for individual control)
manage-services.bat → Option 1
```

### **🛑 Stopping Services**
```bash
# Quick stop (terminates all Java processes)
quick-services.bat stop

# Interactive stop (selective termination)
manage-services.bat → Option 2
```

### **🔍 Status Checking**
```bash
# Quick status check
quick-services.bat status

# Interactive status with details
manage-services.bat → Option 3
```

---

## 📊 **Health Monitoring**

### **✅ Health Check Process**
1. **HTTP Requests**: Uses curl to check `/actuator/health`
2. **Response Analysis**: Parses HTTP response codes
3. **Status Display**: Shows [RUNNING] or [STOPPED] for each service
4. **Error Handling**: Graceful handling of connection failures

### **🎯 Expected Responses**
- **200 OK**: Service is running and healthy
- **Connection Refused**: Service is not running
- **Timeout**: Service is unresponsive

---

## 🛠️ **Troubleshooting**

### **⚠️ Common Issues & Solutions**

#### **Build Failures**
```bash
# Issue: Maven compilation error
# Solution: Check dependencies and Java version
mvn clean compile -X

# Issue: Common module not found
# Solution: Build common module first
cd common && mvn clean install
```

#### **Service Startup Failures**
```bash
# Issue: Port already in use
# Solution: Check for existing processes
netstat -ano | findstr :8080

# Issue: Database connection failure
# Solution: Verify MySQL is running
mysql -u root -p
```

#### **Health Check Failures**
```bash
# Issue: curl command not found
# Solution: Use PowerShell equivalent
Invoke-WebRequest -Uri http://localhost:8080/actuator/health

# Issue: Services not responding
# Solution: Check application logs
# Look for startup errors in service console
```

---

## 🎯 **Best Practices**

### **✅ Development Workflow**
1. **Build First**: `quick-services.bat build`
2. **Start Services**: `quick-services.bat start`
3. **Check Status**: `quick-services.bat status`
4. **Monitor Logs**: Watch service consoles for errors
5. **Test APIs**: Use Postman/curl to test endpoints

### **✅ Production Considerations**
1. **Use Docker**: Containerize services for production
2. **Environment Variables**: Use config files for different environments
3. **Service Discovery**: Ensure Eureka is running before other services
4. **Database**: Verify MySQL connection and schema creation
5. **Load Balancing**: Configure API Gateway for multiple instances

---

## 🚀 **Quick Start Guide**

### **🎯 First Time Setup**
```bash
# 1. Build all services
quick-services.bat build

# 2. Start all services
quick-services.bat start

# 3. Verify all running
quick-services.bat status

# 4. Access services
# Frontend: http://localhost:4200
# API Gateway: http://localhost:9080
# Eureka Dashboard: http://localhost:8761
```

### **🎯 Daily Development**
```bash
# Morning start
quick-services.bat start

# Status check
quick-services.bat status

# Evening stop
quick-services.bat stop
```

---

## 📞 **Support**

### **🔍 Debug Mode**
For detailed debugging, use the interactive menu:
```bash
manage-services.bat
```

This provides:
- Individual service control
- Detailed error messages
- Step-by-step process visibility
- Service-specific troubleshooting

### **📝 Logging**
All service operations are logged in:
- Individual service console windows
- Maven build output
- Health check results
- Error messages with timestamps

---

## 🎉 **Summary**

### **✅ What You Have**
- **Complete Management System**: Start/stop/build/status for all services
- **Error Handling**: Robust failure detection and reporting
- **Health Monitoring**: Real-time service status checking
- **Interactive Control**: Individual service management
- **Quick Commands**: Fast operations for daily use

### **🚀 Ready for Development**
Your RevPlay microservices platform is now fully manageable with comprehensive automation tools!

---

**🎊 Use these scripts to efficiently manage your entire RevPlay microservices platform!**
