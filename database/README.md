# RevPlay Database

This folder contains database schemas, migrations, and setup scripts for the RevPlay music streaming platform.

## Database Structure

The RevPlay platform uses MySQL as the primary database. Each microservice has its own dedicated database:

- `revplay_user_service` - User authentication and profiles
- `revplay_catalog_service` - Music catalog (songs, albums, artists)
- `revplay_playlist_service` - User playlists and follows
- `revplay_favourite_service` - User favorites
- `revplay_playback_service` - Playback history and queues
- `revplay_analytics_service` - Analytics and reporting

## Schema Files

### Individual Service Schemas
- `01_user_service_schema.sql` - User service database schema
- `02_catalog_service_schema.sql` - Catalog service database schema
- `03_playlist_service_schema.sql` - Playlist service database schema
- `04_playback_service_schema.sql` - Playback service database schema
- `05_favourite_service_schema.sql` - Favourite service database schema
- `06_analytics_service_schema.sql` - Analytics service database schema

## Setup Instructions

1. Install MySQL 8.0 or higher
2. Run each schema file to create databases and tables:
   ```bash
   mysql -u root -p < 01_user_service_schema.sql
   mysql -u root -p < 02_catalog_service_schema.sql
   mysql -u root -p < 03_playlist_service_schema.sql
   mysql -u root -p < 04_playback_service_schema.sql
   mysql -u root -p < 05_favourite_service_schema.sql
   mysql -u root -p < 06_analytics_service_schema.sql
   ```
3. Configure connection strings in microservices' application.yml files
4. Start the microservices

## Key Features Implemented

### User Service
- User authentication with JWT
- Role-based access control (LISTENER, ARTIST, ADMIN)
- Profile management with statistics
- Artist profile features

### Catalog Service
- Song, Album, and Artist entities
- Advanced search functionality
- Genre-based browsing
- Artist upload management
- Trending content discovery

### Playlist Service
- Playlist CRUD operations
- Follow/unfollow public playlists
- Song reordering in playlists
- Playlist statistics and discovery

### Playback Service
- Queue management
- Advanced playback controls (shuffle, repeat, volume)
- Recently played tracking
- Listening history and statistics

### Favourite Service
- Like/unlike songs
- Favorite songs management
- Quick access to favorites

### Analytics Service
- Song play analytics
- User listening statistics
- Artist performance metrics
- Trending analytics

## Configuration

Each microservice is configured to connect to its respective database in the `application.yml` files. The databases are designed to be independent but can be joined through service communication when needed.

## Sample Data

Each schema file includes sample data for testing purposes. This includes:
- Test users with different roles
- Sample songs, albums, and artists
- Example playlists and follows
- Playback history and analytics data

## Security Considerations

- Passwords are hashed using BCrypt
- Database connections use SSL in production
- Each service has limited database permissions
- Sensitive data is encrypted at rest
