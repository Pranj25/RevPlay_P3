-- Analytics Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_analytics_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_analytics_service;

-- Song analytics table
CREATE TABLE song_analytics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    song_id BIGINT NOT NULL,
    song_title VARCHAR(200),
    song_artist VARCHAR(500),
    artist_id BIGINT,
    total_plays BIGINT DEFAULT 0,
    unique_listeners INT DEFAULT 0,
    total_play_time BIGINT DEFAULT 0,
    average_play_duration DECIMAL(10,2),
    completion_rate DECIMAL(5,2),
    last_played_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_song_id (song_id),
    INDEX idx_artist_id (artist_id),
    INDEX idx_total_plays (total_plays),
    INDEX idx_last_played_at (last_played_at)
);

-- User analytics table
CREATE TABLE user_analytics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_songs_played INT DEFAULT 0,
    total_play_time BIGINT DEFAULT 0,
    unique_songs_played INT DEFAULT 0,
    favorite_genre VARCHAR(100),
    average_session_duration DECIMAL(10,2),
    total_sessions INT DEFAULT 0,
    last_active_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_total_songs_played (total_songs_played),
    INDEX idx_last_active_at (last_active_at)
);

-- Sample data for testing
INSERT INTO song_analytics (song_id, song_title, song_artist, artist_id, total_plays, unique_listeners, last_played_at) VALUES
(1, 'Amazing Song', 'John Doe', 1, 5, 2, NOW()),
(2, 'Pop Hit', 'Jane Smith', 2, 3, 2, NOW());

INSERT INTO user_analytics (user_id, total_songs_played, total_play_time, unique_songs_played, last_active_at) VALUES
(1, 3, 600, 2, NOW()),
(2, 2, 300, 2, NOW());
