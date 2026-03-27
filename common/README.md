# Common Library

## Overview
The Common Library provides shared components for inter-service communication in the RevPlay microservices architecture. It includes DTOs, Feign clients, and utility services for REST API communication between services.

## Features
- **Shared DTOs**: Common data transfer objects for consistency
- **Feign Clients**: Declarative REST clients for service communication
- **Service Communication**: Centralized communication service
- **Error Handling**: Consistent error handling across services
- **Configuration**: Common Feign configuration

## Components

### DTOs (Data Transfer Objects)
- `UserDto` - User information transfer
- `SongDto` - Song information transfer

### Feign Clients
- `UserServiceClient` - Communication with User Service
- `CatalogServiceClient` - Communication with Catalog Service
- `FavouriteServiceClient` - Communication with Favourite Service
- `PlaylistServiceClient` - Communication with Playlist Service
- `AnalyticsServiceClient` - Communication with Analytics Service

### Service Layer
- `ServiceCommunicationService` - Centralized communication service
- `FeignConfig` - Feign client configuration

## Usage

### Add Common Library Dependency
Add this to your service's `pom.xml`:
```xml
<dependency>
    <groupId>com.revplay</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Enable Feign Clients
Add to your main application class:
```java
@EnableFeignClients(basePackages = "com.revplay.common.feign")
```

### Use Service Communication
```java
@Autowired
private ServiceCommunicationService communicationService;

// Get user information
UserDto user = communicationService.getUserById(userId);

// Get song information
SongDto song = communicationService.getSongById(songId);

// Record analytics
communicationService.recordSongPlay(songId, title, artist, duration, completed);
```

## Service Communication Examples

### User Service Integration
```java
// Validate user exists
if (!communicationService.userExists(userId)) {
    throw new UserNotFoundException("User not found");
}

// Get user details
UserDto user = communicationService.getUserById(userId);
```

### Catalog Service Integration
```java
// Get song details
SongDto song = communicationService.getSongById(songId);

// Increment play count
communicationService.incrementPlayCount(songId);

// Get user's uploaded songs
List<SongDto> userSongs = communicationService.getSongsByUser(userId);
```

### Favourite Service Integration
```java
// Check if song is liked
boolean isLiked = communicationService.isSongLiked(userId, songId);

// Get like count
Map<String, Long> likeCount = communicationService.getSongLikeCount(songId);

// Get multiple like counts
Map<Long, Long> likeCounts = communicationService.getMultipleSongLikeCounts(songIds);
```

### Analytics Integration
```java
// Record song play
communicationService.recordSongPlay(songId, title, artist, duration, completed);

// Record user activity
communicationService.recordUserActivity(userId, playTime, songsPlayed, uniqueSongs, newSession);

// Get top songs
List<Object[]> topSongs = communicationService.getTopPlayedSongs(30, 10);
```

## Configuration

### Feign Configuration
- **Logger Level**: FULL logging for debugging
- **Retry Policy**: 3 retries with 100ms delay
- **Timeout**: Default timeouts with retry mechanism

### Load Balancing
Services are automatically load-balanced through Eureka service discovery.

## Error Handling

### Service Unavailable
All service calls are wrapped in try-catch blocks with graceful degradation:
- User/Song existence checks return false if service is unavailable
- Analytics recording fails silently to not impact main functionality
- Critical operations throw runtime exceptions

### Response Handling
- Consistent error messages across services
- Default values for missing data
- Proper exception propagation for critical failures

## Best Practices

### Service Communication
1. Always check service availability before making calls
2. Use the centralized `ServiceCommunicationService` for consistency
3. Handle failures gracefully with fallback values
4. Log errors for debugging but don't fail main operations

### Performance
1. Use batch operations where available (e.g., multiple like counts)
2. Cache frequently accessed data
3. Implement circuit breakers for critical services
4. Monitor service communication metrics

### Security
1. Pass authentication tokens through headers
2. Validate service-to-service communication
3. Use HTTPS for inter-service communication
4. Implement rate limiting for service calls

## Building the Common Library
```bash
cd common
mvn clean install
```

This will install the common library to your local Maven repository for use in other services.
