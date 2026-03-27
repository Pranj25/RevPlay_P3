# Analytics Service

## Overview
The Analytics Service provides comprehensive analytics for the RevPlay application, including views count, trends analysis, and detailed reports for songs, users, and platform-wide metrics.

## Features
- **Views Count**: Track song plays, user interactions, and engagement
- **Trends Analysis**: Identify trending songs, artists, and genres
- **Reports**: Generate detailed reports for songs, users, and platform
- **Real-time Analytics**: Track user activity and song performance
- **Historical Data**: Maintain historical analytics for trend analysis
- **Multi-dimensional Metrics**: Plays, likes, shares, session duration, and more

## Configuration
- **Port**: 8089
- **Database**: MySQL (revplay_analytics_db)
- **Service Registration**: Eureka Server (localhost:8761)
- **Configuration Server**: Config Server (localhost:8888)

## API Endpoints

### Song Analytics Recording
- `POST /analytics/song/play` - Record song play
- `POST /analytics/song/like` - Record song like
- `POST /analytics/song/share` - Record song share

### User Analytics Recording
- `POST /analytics/user/activity` - Record user activity
- `POST /analytics/user/like` - Record user like
- `POST /analytics/user/playlist` - Record playlist creation

### Reports
- `GET /analytics/reports/song/{songId}` - Get song analytics report
- `GET /analytics/reports/user/{userId}` - Get user analytics report
- `GET /analytics/reports/platform` - Get platform analytics report

### Trends
- `GET /analytics/trends/top-songs` - Get top played songs
- `GET /analytics/trends/top-artists` - Get top artists
- `GET /analytics/trends/top-genres` - Get top genres
- `GET /analytics/trends/top-users` - Get most active users

### Summary Statistics
- `GET /analytics/summary/song/{songId}` - Get song summary
- `GET /analytics/summary/platform` - Get platform summary
- `GET /analytics/summary/user/{userId}` - Get user summary

## Request Examples

### Record Song Play
```
POST /analytics/song/play?songId=123&songTitle=Amazing Song&songArtist=Great Artist&duration=240&completed=true
```

### Record User Activity
```
POST /analytics/user/activity?userId=1&playTime=1800&songsPlayed=5&uniqueSongs=3&newSession=true
```

### Get Top Songs
```
GET /analytics/trends/top-songs?days=30&limit=10
```

### Get Platform Report
```
GET /analytics/reports/platform?startDate=2024-01-01T00:00:00
```

## Response Examples

### Song Play Recording
```json
{
  "message": "Song play recorded successfully"
}
```

### Top Songs Response
```json
[
  [123, 15420],
  [456, 12350],
  [789, 10200]
]
```

### Platform Summary Response
```json
{
  "period": "30 days",
  "totalPlays": 125000,
  "uniqueSongsPlayed": 8500,
  "activeUsers": 3200,
  "averageSessionDuration": 125.5
}
```

### Daily Platform Stats Response
```json
[
  ["2024-01-01", 45000, 1200, 850],
  ["2024-01-02", 48000, 1350, 920],
  ["2024-01-03", 52000, 1400, 980]
]
```

## Running Analytics Service
```bash
cd analytics-service
mvn spring-boot:run
```

## Database Schema
The service creates the following tables:
- `song_analytics` - Daily song performance metrics
- `user_analytics` - Daily user activity metrics

## Analytics Features

### Song Analytics
- **Play Count**: Total plays per day
- **Unique Listeners**: Number of unique users
- **Completion Rate**: Average song completion
- **Engagement**: Likes, shares, downloads
- **Performance**: Duration and trends

### User Analytics
- **Play Time**: Total listening time
- **Session Metrics**: Number and duration of sessions
- **Engagement**: Songs liked, playlists created
- **Activity Patterns**: Daily and hourly usage
- **Retention**: User engagement over time

### Platform Analytics
- **Usage Patterns**: Peak hours and days
- **Content Performance**: Top songs, artists, genres
- **User Engagement**: Active users and retention
- **Growth Metrics**: New users and content consumption

### Trend Analysis
- **Trending Content**: Rising songs and artists
- **Genre Popularity**: Music preference trends
- **User Behavior**: Listening pattern changes
- **Seasonal Patterns**: Time-based usage trends

## Performance Considerations
- Daily aggregation for efficient querying
- Indexed queries for fast trend analysis
- Batch processing for historical data
- Caching for frequently accessed metrics
- Scheduled jobs for data cleanup and aggregation
