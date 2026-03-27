# Favourite Service

## Overview
The Favourite Service manages song likes/unlikes for the RevPlay application. Users can like songs, unlike songs, and view favourite songs and statistics.

## Features
- **Like Songs**: Add songs to user favourites
- **Unlike Songs**: Remove songs from user favourites
- **Toggle Like**: Quick toggle between like/unlike
- **Check Status**: Check if a song is liked by a user
- **User Favourites**: Get all favourite songs for a user
- **Song Statistics**: Get like counts and most liked songs
- **Batch Operations**: Get like counts for multiple songs

## Configuration
- **Port**: 8087
- **Database**: MySQL (revplay_favourite_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## API Endpoints

### Like/Unlike Operations
- `POST /favourites/like` - Like a song
- `DELETE /favourites/unlike?userId={id}&songId={id}` - Unlike a song
- `POST /favourites/toggle` - Toggle like/unlike status

### Status Check
- `GET /favourites/check?userId={id}&songId={id}` - Check if song is liked

### User Operations
- `GET /favourites/user/{userId}` - Get user's favourite songs
- `GET /favourites/user/{userId}/count` - Get user's favourite count

### Song Operations
- `GET /favourites/song/{songId}` - Get users who liked the song
- `GET /favourites/song/{songId}/count` - Get song like count

### Statistics & Analytics
- `POST /favourites/batch/likes` - Get like counts for multiple songs
- `GET /favourites/most-liked` - Get most liked songs

## Request Examples

### Like a Song
```json
POST /favourites/like
{
  "userId": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist"
}
```

### Unlike a Song
```
DELETE /favourites/unlike?userId=1&songId=123
```

### Toggle Like
```json
POST /favourites/toggle
{
  "userId": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist"
}
```

### Check Like Status
```
GET /favourites/check?userId=1&songId=123
```

### Batch Like Counts
```json
POST /favourites/batch/likes
[123, 456, 789]
```

## Response Examples

### Like Response
```json
{
  "id": 1,
  "userId": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist",
  "likedAt": "2024-01-01T10:30:00"
}
```

### Toggle Like Response
```json
{
  "liked": true,
  "message": "Song added to favourites",
  "favourite": {
    "id": 1,
    "userId": 1,
    "songId": 123,
    "songTitle": "Amazing Song",
    "songArtist": "Great Artist",
    "likedAt": "2024-01-01T10:30:00"
  }
}
```

### Check Status Response
```json
{
  "liked": true
}
```

### Like Count Response
```json
{
  "count": 42
}
```

## Running Favourite Service
```bash
cd favourite-service
mvn spring-boot:run
```

## Database Schema
The service creates the following table:
- `favourites` - User-song favourite relationships with unique constraint

## Features Details

### Duplicate Prevention
- Database unique constraint prevents duplicate likes
- Service-level validation for better error messages

### Performance Optimization
- Efficient queries for like counts
- Batch operations for multiple songs
- Indexed on userId and songId

### Analytics
- Most liked songs tracking
- User engagement metrics
- Song popularity analytics
