-- Favourite Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_favourite_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_favourite_service;

-- Favourites table
CREATE TABLE favourites (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    song_title VARCHAR(200),
    song_artist VARCHAR(500),
    liked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_song_id (song_id),
    INDEX idx_liked_at (liked_at),
    
    UNIQUE KEY unique_user_song_favourite (user_id, song_id)
);

-- Sample data for testing
INSERT INTO favourites (user_id, song_id, song_title, song_artist) VALUES
(1, 1, 'Amazing Song', 'John Doe'),
(1, 2, 'Pop Hit', 'Jane Smith'),
(2, 1, 'Amazing Song', 'John Doe');
