-- RevPlay Database Creation Script
-- Creates all required databases for the RevPlay music streaming platform

-- Create user database
CREATE DATABASE IF NOT EXISTS revplay_user_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create catalog database
CREATE DATABASE IF NOT EXISTS revplay_catalog_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create playlist database
CREATE DATABASE IF NOT EXISTS revplay_playlist_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create favourite database
CREATE DATABASE IF NOT EXISTS revplay_favourite_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create playback database
CREATE DATABASE IF NOT EXISTS revplay_playback_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create analytics database
CREATE DATABASE IF NOT EXISTS revplay_analytics_db 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Show created databases
SHOW DATABASES LIKE 'revplay_%';
