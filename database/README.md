# RevPlay Database

This folder contains database schemas, migrations, and setup scripts for the RevPlay music streaming platform.

## Database Structure

The RevPlay platform uses MySQL as the primary database. The following databases are used:

- `revplay_user_db` - User authentication and profiles
- `revplay_catalog_db` - Music catalog (songs, albums, artists)
- `revplay_playlist_db` - User playlists
- `revplay_favourite_db` - User favorites
- `revplay_playback_db` - Playback history and analytics
- `revplay_analytics_db` - Analytics and reporting

## Setup Instructions

1. Install MySQL 8.0 or higher
2. Create databases using the provided SQL scripts
3. Configure connection strings in microservices
4. Run migration scripts

## SQL Scripts

- `01_create_databases.sql` - Creates all required databases
- `02_create_tables.sql` - Creates all tables
- `03_insert_sample_data.sql` - Inserts sample data for testing

## Configuration

Each microservice is configured to connect to its respective database in the `application.yml` files.
