-- Playlist Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_playlist_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_playlist_service;

-- Playlists table
CREATE TABLE playlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,
    cover_image_path TEXT,
    follower_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_name (name),
    INDEX idx_is_public (is_public),
    INDEX idx_created_at (created_at),
    
    UNIQUE KEY unique_user_playlist_name (user_id, name)
);

-- Playlist songs table
CREATE TABLE playlist_songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    song_title VARCHAR(200),
    song_artist VARCHAR(500),
    position_order INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_playlist_id (playlist_id),
    INDEX idx_song_id (song_id),
    INDEX idx_position_order (position_order),
    
    UNIQUE KEY unique_playlist_song (playlist_id, song_id)
);

-- Playlist follows table
CREATE TABLE playlist_follows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    playlist_id BIGINT NOT NULL,
    followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_playlist_id (playlist_id),
    
    UNIQUE KEY unique_user_playlist_follow (user_id, playlist_id)
);

-- Sample data for testing
INSERT INTO playlists (name, description, user_id, is_public) VALUES
('My Favorites', 'My favorite songs collection', 1, FALSE),
('Public Playlist', 'Share with everyone', 2, TRUE),
('Rock Classics', 'Best rock songs ever', 1, TRUE);

INSERT INTO playlist_songs (playlist_id, song_id, song_title, song_artist, position_order) VALUES
(1, 1, 'Amazing Song', 'John Doe', 1),
(2, 2, 'Pop Hit', 'Jane Smith', 1),
(3, 1, 'Amazing Song', 'John Doe', 1);

INSERT INTO playlist_follows (user_id, playlist_id) VALUES
(1, 2),  -- John follows Jane's public playlist
(2, 3);  -- Jane follows John's public playlist
