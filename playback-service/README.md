# Playback Service

## Overview
The Playback Service handles music streaming logic and media handling for the RevPlay application. It manages audio streaming, playback tracking, and play history analytics.

## Features
- **Media Streaming**: Stream audio files with range request support
- **Playback Tracking**: Record user play history with detailed analytics
- **Format Support**: Multiple audio formats (MP3, WAV, FLAC, AAC, OGG)
- **Device Detection**: Track playback across different devices
- **Analytics**: Comprehensive play statistics and insights
- **Range Requests**: Support for partial content streaming

## Configuration
- **Port**: 8088
- **Database**: MySQL (revplay_playback_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## Media Configuration
- **Chunk Size**: 8KB (configurable)
- **Buffer Size**: 16KB (configurable)
- **Supported Formats**: MP3, WAV, FLAC, AAC, OGG

## API Endpoints

### Streaming
- `GET /playback/stream/{songId}?filePath={path}` - Stream audio file
- `GET /playback/stream/info/{songId}?filePath={path}` - Get stream information

### Playback Tracking
- `POST /playback/record` - Record playback event

### Play History
- `GET /playback/history/user/{userId}` - Get user play history
- `GET /playback/history/user/{userId}/range` - Get user play history in date range
- `GET /playback/history/song/{songId}` - Get song play history

### Statistics
- `GET /playback/stats/user/{userId}/total` - Get user total plays
- `GET /playback/stats/song/{songId}/total` - Get song total plays
- `GET /playback/stats/user/{userId}/song/{songId}` - Get user-song play count
- `GET /playback/stats/most-played` - Get most played songs
- `GET /playback/stats/user/{userId}/most-played` - Get user's most played songs
- `GET /playback/stats/user/{userId}/daily` - Get daily play statistics

## Request Examples

### Record Playback
```json
POST /playback/record
{
  "userId": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist",
  "filePath": "./uploads/music/uuid.mp3",
  "durationSeconds": 240,
  "playedDurationSeconds": 180,
  "completed": false
}
```

### Stream Audio
```
GET /playback/stream/123?filePath=./uploads/music/uuid.mp3
Range: bytes=0-8191
```

### Get Stream Info
```
GET /playback/stream/info/123?filePath=./uploads/music/uuid.mp3
```

## Response Examples

### Playback Record Response
```json
{
  "id": 1,
  "userId": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist",
  "playedAt": "2024-01-01T10:30:00",
  "durationSeconds": 240,
  "playedDurationSeconds": 180,
  "completed": false,
  "deviceType": "Desktop",
  "ipAddress": "192.168.1.100"
}
```

### Stream Info Response
```json
{
  "songId": 123,
  "filePath": "./uploads/music/uuid.mp3",
  "fileSize": 5242880,
  "format": "mp3",
  "streamable": true
}
```

### Statistics Response
```json
{
  "totalPlays": 42
}
```

## Running Playback Service
```bash
cd playback-service
mvn spring-boot:run
```

## Database Schema
The service creates the following table:
- `play_history` - Detailed playback tracking with analytics

## Streaming Features

### Range Request Support
- HTTP 206 Partial Content responses
- Byte-range streaming for efficient playback
- Support for seeking in audio players

### Format Validation
- Automatic format detection
- Support for multiple audio formats
- Format-specific MIME types

### Analytics & Tracking
- Device type detection (Mobile, Tablet, Desktop)
- IP address tracking
- Play completion tracking
- Duration analysis

## Performance Optimizations
- Configurable chunk sizes for streaming
- Efficient file I/O with RandomAccessFile
- Memory-efficient streaming buffers
- Optimized database queries for analytics
