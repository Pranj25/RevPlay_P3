# Catalog Service

## Overview
The Catalog Service manages music catalog functionality including song upload, metadata management, and file storage for the RevPlay application.

## Features
- **Song Upload**: Upload music files with metadata
- **File Storage**: Store music files and cover images in system directories
- **Song Metadata**: Manage song information (title, artist, album, genre, etc.)
- **Search & Filter**: Search songs by title, artist, album, genre
- **Play Count Tracking**: Track song play statistics
- **User Songs**: Manage songs uploaded by specific users

## Configuration
- **Port**: 8082
- **Database**: MySQL (revplay_catalog_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)
- **File Storage**: Local system directories

## File Storage Directories
- **Music Files**: `./uploads/music/`
- **Cover Images**: `./uploads/covers/`

## API Endpoints

### Song Management
- `POST /songs/upload` - Upload new song with metadata
- `GET /songs/{id}` - Get song by ID
- `GET /songs` - Get all public songs
- `PUT /songs/{id}` - Update song metadata
- `DELETE /songs/{id}` - Delete song

### Search & Filter
- `GET /songs/search?q={query}` - Search songs
- `GET /songs/genre/{genre}` - Get songs by genre
- `GET /songs/artist/{artist}` - Get songs by artist
- `GET /songs/user/{userId}` - Get songs uploaded by user

### Statistics & Discovery
- `GET /songs/most-played` - Get most played songs
- `GET /songs/latest` - Get latest uploaded songs
- `POST /songs/{id}/play` - Increment play count

### File Access
- `GET /songs/download/{id}` - Download music file
- `GET /songs/cover/{id}` - Get cover image

## Request Examples

### Upload Song (multipart/form-data)
```
POST /songs/upload
Content-Type: multipart/form-data

songData: {
  "title": "My Song",
  "artist": "Artist Name",
  "album": "Album Name",
  "genre": "Pop",
  "durationSeconds": 240,
  "uploadedBy": 1,
  "isPublic": true
}
musicFile: [music file]
coverImage: [cover image file] (optional)
```

### Search Songs
```
GET /songs/search?q=love
```

## Response Examples

### Song Upload Response
```json
{
  "id": 1,
  "title": "My Song",
  "artist": "Artist Name",
  "album": "Album Name",
  "genre": "Pop",
  "durationSeconds": 240,
  "filePath": "./uploads/music/uuid.mp3",
  "fileSize": 5242880,
  "fileFormat": "mp3",
  "coverImagePath": "./uploads/covers/uuid.jpg",
  "uploadedBy": 1,
  "isPublic": true,
  "playCount": 0,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## Running Catalog Service
```bash
cd catalog-service
mvn spring-boot:run
```

## Database Schema
The service creates the following table:
- `songs` - Song metadata and file information

## Supported File Formats
- **Music**: MP3, WAV, FLAC, AAC
- **Cover Images**: JPG, PNG, GIF
