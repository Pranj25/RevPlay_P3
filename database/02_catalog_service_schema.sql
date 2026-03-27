-- Catalog Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_catalog_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_catalog_service;

-- Artists table
CREATE TABLE artists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    artist_name VARCHAR(200) NOT NULL,
    bio TEXT,
    profile_image_path TEXT,
    banner_image_path TEXT,
    genre VARCHAR(100),
    total_songs INT DEFAULT 0,
    total_plays BIGINT DEFAULT 0,
    total_followers INT DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    instagram_url TEXT,
    twitter_url TEXT,
    youtube_url TEXT,
    spotify_url TEXT,
    website_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_artist_name (artist_name),
    INDEX idx_genre (genre),
    INDEX idx_is_active (is_active),
    INDEX idx_is_verified (is_verified)
);

-- Albums table
CREATE TABLE albums (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    release_date TIMESTAMP,
    cover_image_path TEXT,
    artist_id BIGINT NOT NULL,
    total_songs INT DEFAULT 0,
    total_duration INT DEFAULT 0,
    is_public BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_artist_id (artist_id),
    INDEX idx_name (name),
    INDEX idx_is_public (is_public),
    INDEX idx_release_date (release_date)
);

-- Songs table
CREATE TABLE songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    artist VARCHAR(500),
    album VARCHAR(200),
    genre VARCHAR(100),
    lyrics TEXT,
    duration_seconds INT,
    file_path TEXT NOT NULL,
    album_id BIGINT,
    artist_id BIGINT NOT NULL,
    uploaded_by BIGINT NOT NULL,
    file_size BIGINT,
    file_format VARCHAR(10),
    cover_image_path TEXT,
    is_public BOOLEAN DEFAULT TRUE,
    play_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_title (title),
    INDEX idx_artist (artist),
    INDEX idx_album (album),
    INDEX idx_genre (genre),
    INDEX idx_album_id (album_id),
    INDEX idx_artist_id (artist_id),
    INDEX idx_uploaded_by (uploaded_by),
    INDEX idx_is_public (is_public),
    INDEX idx_play_count (play_count),
    INDEX idx_created_at (created_at),
    
    FULLTEXT KEY ft_search (title, artist, album, genre)
);

-- Sample data for testing
INSERT INTO artists (user_id, artist_name, genre, bio) VALUES
(2, 'Jane Smith', 'Pop', 'Rising pop artist with a unique sound'),
(1, 'John Doe', 'Rock', 'Rock music enthusiast and creator');

INSERT INTO albums (name, artist_id, description, is_public) VALUES
('First Album', 1, 'Debut album with amazing tracks', TRUE),
('Greatest Hits', 2, 'Best collection of pop songs', TRUE);

INSERT INTO songs (title, artist, album, genre, duration_seconds, file_path, artist_id, uploaded_by) VALUES
('Amazing Song', 'John Doe', 'First Album', 'Rock', 240, '/songs/amazing.mp3', 1, 1),
('Pop Hit', 'Jane Smith', 'Greatest Hits', 'Pop', 180, '/songs/pop_hit.mp3', 2, 2);
