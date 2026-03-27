# 🚀 RevPlay Platform - Quick Start Guide

## 🎯 **Single Command Control**

### **🎮 Main Control Script**
```bash
# Launch interactive menu
revplay-control.bat
```

**This one script gives you complete control over:**
- ✅ All 10 backend services
- ✅ Angular frontend application
- ✅ Build operations with error handling
- ✅ Start/stop/status checking
- ✅ Individual service control
- ✅ Health monitoring

---

## 🚀 **Quick Daily Operations**

### **🌅 Morning Start**
```bash
# Option 1: Build all services and frontend
revplay-control.bat → Option 1

# Option 2: Start all services and frontend  
revplay-control.bat → Option 2
```

### **🌍 Evening Stop**
```bash
# Option 3: Stop all services and frontend
revplay-control.bat → Option 3
```

### **🔍 Status Check**
```bash
# Option 4: Check status of all services and frontend
revplay-control.bat → Option 4
```

---

## 📋 **Complete Menu Options**

### **🎮 RevPlay Platform Controller Menu**
```
1. Build All Services + Frontend     - Build everything with error checking
2. Start All Services + Frontend     - Start all 11 components
3. Stop All Services + Frontend      - Stop all processes
4. Check Status of All Services     - Health check all components
5. Build Backend Only              - Build only backend services
6. Start Backend Only              - Start only backend services
7. Stop Backend Only               - Stop only backend services
8. Check Backend Status Only       - Check only backend services
9. Frontend Only Operations         - Frontend-specific operations
10. Exit                           - Exit the controller
```

---

## 🌐 **Access Points**

### **📡 Backend Services**
| Service | Port | URL | Health Check |
|---------|-------|------|--------------|
| Config Server | 8888 | http://localhost:8888/actuator/health |
| Eureka Server | 8761 | http://localhost:8761/actuator/health |
| API Gateway | 9080 | http://localhost:9080/actuator/health |
| User Service | 8081 | http://localhost:8081/actuator/health |
| Catalog Service | 8082 | http://localhost:8082/actuator/health |
| Playlist Service | 8083 | http://localhost:8083/actuator/health |
| Favourite Service | 8087 | http://localhost:8087/actuator/health |
| Playback Service | 8085 | http://localhost:8085/actuator/health |
| Analytics Service | 8086 | http://localhost:8086/actuator/health |

### **🎨 Frontend**
| Component | Port | URL |
|----------|-------|------|
| Angular App | 4200 | http://localhost:4200 |
| Eureka Dashboard | 8761 | http://localhost:8761 |

---

## 🎯 **Recommended Workflow**

### **✅ Development Setup**
```bash
# Step 1: Build everything
revplay-control.bat → Option 1

# Step 2: Start everything
revplay-control.bat → Option 2

# Step 3: Verify running
revplay-control.bat → Option 4

# Step 4: Access applications
# Frontend: http://localhost:4200
# API Gateway: http://localhost:9080
# Eureka: http://localhost:8761
```

### **✅ Testing Workflow**
```bash
# Build and start specific service
revplay-control.bat → Option 9 → Option 2 (for frontend)
revplay-control.bat → Option 5 → Option 6 (for specific backend)

# Check individual service status
revplay-control.bat → Option 4
```

---

## 🛠️ **Features Included**

### **🔧 Build Operations**
- ✅ **Error Handling**: Stops on first failure with detailed error message
- ✅ **Dependency Resolution**: Builds common module first
- ✅ **Sequential Build**: Proper order for dependencies
- ✅ **Success Confirmation**: Each service confirms successful build
- ✅ **Frontend Build**: npm install + ng build with legacy peer deps

### **🚀 Start Operations**
- ✅ **Sequential Startup**: Services start with delays to prevent conflicts
- ✅ **Named Windows**: Each service runs in titled command window
- ✅ **Process Management**: Clean startup and shutdown procedures
- ✅ **Frontend Dev**: Angular development server with live reload

### **🔍 Status Operations**
- ✅ **Health Checks**: Uses `/actuator/health` endpoints
- ✅ **Real-time Status**: Shows [RUNNING] or [STOPPED] for each service
- ✅ **Connection Testing**: Graceful handling of connection failures
- ✅ **Comprehensive Coverage**: All 11 components checked

### **🛑 Stop Operations**
- ✅ **Clean Shutdown**: Terminates all Java and Node.js processes
- ✅ **Process Cleanup**: Removes orphaned command processes
- ✅ **Selective Stop**: Can stop individual services or frontend only
- ✅ **Safe Termination**: Uses proper Windows process termination

---

## 🎊 **You're Ready!**

### **✅ What You Have Now**
- 🎮 **Complete Control**: One script to manage everything
- 🔧 **Build Automation**: Error-checked builds for all services
- 🚀 **Startup Automation**: Sequential service startup
- 🔍 **Status Monitoring**: Real-time health checks
- 🛑 **Clean Management**: Proper shutdown procedures

### **✅ How to Use**
```bash
# Navigate to project directory
cd "c:\Users\gaytr\OneDrive\Desktop\revplay microservices"

# Run the controller
revplay-control.bat

# Choose your operation from the menu
# Follow the on-screen prompts
```

### **✅ Expected Results**
- 🟢 **Fast Development**: Quick build/start/stop cycles
- 🟢 **Error Detection**: Immediate feedback on build failures
- 🟢 **Service Health**: Real-time status of all components
- 🟢 **Local Development**: Complete platform running locally
- 🟢 **Production Ready**: Built artifacts ready for deployment

---

## 🎯 **First Time Setup**

### **🔧 Initial Setup**
```bash
# 1. Ensure you have prerequisites:
#    - Java 17+
#    - Maven 3.6+
#    - Node.js 18+
#    - MySQL Server
#    - Git

# 2. Build everything first:
revplay-control.bat → Option 1

# 3. Start all services:
revplay-control.bat → Option 2

# 4. Verify everything works:
revplay-control.bat → Option 4

# 5. Access your applications:
#    Frontend: http://localhost:4200
#    Backend APIs: http://localhost:9080 (gateway)
#    Service Registry: http://localhost:8761
```

---

**🎉 Your RevPlay platform is now fully controllable with a single comprehensive script!**

**Run `revplay-control.bat` to manage your entire microservices platform efficiently!**
