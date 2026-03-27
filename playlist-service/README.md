# Playlist Service

## Overview
The Playlist Service manages playlist creation and song management for the RevPlay application. Users can create playlists, add/remove songs, and manage their music collections.

## Features
- **Create Playlist**: Create new playlists with name, description, and privacy settings
- **Add/Remove Songs**: Add songs to playlists and remove them
- **Playlist Management**: Update playlist details and delete playlists
- **Search & Discovery**: Search playlists and discover public playlists
- **Song Ordering**: Maintain song order within playlists
- **Privacy Control**: Public and private playlist support

## Configuration
- **Port**: 8086
- **Database**: MySQL (revplay_playlist_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## API Endpoints

### Playlist Management
- `POST /playlists` - Create new playlist
- `GET /playlists/{id}` - Get playlist by ID
- `PUT /playlists/{id}` - Update playlist details
- `DELETE /playlists/{id}` - Delete playlist

### User Playlists
- `GET /playlists/user/{userId}` - Get all user playlists
- `GET /playlists/user/{userId}/public` - Get user's public playlists

### Discovery & Search
- `GET /playlists/public` - Get all public playlists
- `GET /playlists/search?q={query}` - Search playlists

### Song Management
- `POST /playlists/{playlistId}/songs` - Add song to playlist
- `DELETE /playlists/{playlistId}/songs/{songId}` - Remove song from playlist
- `GET /playlists/{playlistId}/songs` - Get all songs in playlist
- `GET /playlists/{playlistId}/songs/{songId}/exists` - Check if song exists in playlist
- `GET /playlists/{playlistId}/count` - Get song count in playlist

## Request Examples

### Create Playlist
```json
POST /playlists
{
  "name": "My Favorite Songs",
  "description": "Collection of my favorite tracks",
  "userId": 1,
  "isPublic": false
}
```

### Add Song to Playlist
```json
POST /playlists/1/songs
{
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist"
}
```

### Update Playlist
```json
PUT /playlists/1
{
  "name": "Updated Playlist Name",
  "description": "New description",
  "isPublic": true
}
```

## Response Examples

### Playlist Response
```json
{
  "id": 1,
  "name": "My Favorite Songs",
  "description": "Collection of my favorite tracks",
  "userId": 1,
  "isPublic": false,
  "coverImagePath": null,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

### Playlist Song Response
```json
{
  "id": 1,
  "songId": 123,
  "songTitle": "Amazing Song",
  "songArtist": "Great Artist",
  "addedAt": "2024-01-01T10:30:00",
  "positionOrder": 1
}
```

## Running Playlist Service
```bash
cd playlist-service
mvn spring-boot:run
```

## Database Schema
The service creates the following tables:
- `playlists` - Playlist information
- `playlist_songs` - Many-to-many relationship between playlists and songs

## Features Details

### Song Ordering
- Songs are automatically ordered when added to playlists
- Position is maintained when songs are removed
- Reordering ensures continuous sequence

### Privacy Control
- Private playlists: Only visible to owner
- Public playlists: Discoverable by all users

### Validation
- Playlist names must be unique per user
- Duplicate songs are prevented in playlists
- All required fields are validated
