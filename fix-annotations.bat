@echo off
echo Fixing deprecated EnableEurekaClient annotations...

echo Fixing user-service...
powershell -Command "(Get-Content 'user-service\src\main\java\com\revplay\userservice\UserServiceApplication.java') -replace 'EnableEurekaClient', 'EnableDiscoveryClient' -replace 'org.springframework.cloud.netflix.eureka.EnableEurekaClient', 'org.springframework.cloud.client.discovery.EnableDiscoveryClient' | Set-Content 'user-service\src\main\java\com\revplay\userservice\UserServiceApplication.java'"

echo Fixing catalog-service...
powershell -Command "(Get-Content 'catalog-service\src\main\java\com\revplay\catalogservice\CatalogServiceApplication.java') -replace 'EnableEurekaClient', 'EnableDiscoveryClient' -replace 'org.springframework.cloud.netflix.eureka.EnableEurekaClient', 'org.springframework.cloud.client.discovery.EnableDiscoveryClient' | Set-Content 'catalog-service\src\main\java\com\revplay\catalogservice\CatalogServiceApplication.java'"

echo Fixing playlist-service...
powershell -Command "(Get-Content 'playlist-service\src\main\java\com\revplay\playlistservice\PlaylistServiceApplication.java') -replace 'EnableEurekaClient', 'EnableDiscoveryClient' -replace 'org.springframework.cloud.netflix.eureka.EnableEurekaClient', 'org.springframework.cloud.client.discovery.EnableDiscoveryClient' | Set-Content 'playlist-service\src\main\java\com\revplay\playlistservice\PlaylistServiceApplication.java'"

echo Fixing playback-service...
powershell -Command "(Get-Content 'playback-service\src\main\java\com\revplay\playbackservice\PlaybackServiceApplication.java') -replace 'EnableEurekaClient', 'EnableDiscoveryClient' -replace 'org.springframework.cloud.netflix.eureka.EnableEurekaClient', 'org.springframework.cloud.client.discovery.EnableDiscoveryClient' | Set-Content 'playback-service\src\main\java\com\revplay\playbackservice\PlaybackServiceApplication.java'"

echo Fixing analytics-service...
powershell -Command "(Get-Content 'analytics-service\src\main\java\com\revplay\analyticsservice\AnalyticsServiceApplication.java') -replace 'EnableEurekaClient', 'EnableDiscoveryClient' -replace 'org.springframework.cloud.netflix.eureka.EnableEurekaClient', 'org.springframework.cloud.client.discovery.EnableDiscoveryClient' | Set-Content 'analytics-service\src\main\java\com\revplay\analyticsservice\AnalyticsServiceApplication.java'"

echo All annotations fixed!
pause
