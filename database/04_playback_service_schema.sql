-- Playback Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_playback_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_playback_service;

-- Playback queues table
CREATE TABLE playback_queues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    current_song_id BIGINT,
    current_position_seconds INT DEFAULT 0,
    is_playing BOOLEAN DEFAULT FALSE,
    is_paused BOOLEAN DEFAULT FALSE,
    volume INT DEFAULT 100,
    repeat_mode ENUM('OFF', 'ONE', 'ALL') DEFAULT 'OFF',
    is_shuffle BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_user_id (user_id),
    INDEX idx_current_song_id (current_song_id),
    INDEX idx_is_playing (is_playing)
);

-- Queue items table
CREATE TABLE queue_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    queue_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    song_title VARCHAR(200),
    song_artist VARCHAR(500),
    position_in_queue INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_queue_id (queue_id),
    INDEX idx_song_id (song_id),
    INDEX idx_position_in_queue (position_in_queue),
    
    UNIQUE KEY unique_queue_position (queue_id, position_in_queue)
);

-- Play history table
CREATE TABLE play_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    song_title VARCHAR(200),
    song_artist VARCHAR(500),
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    duration_seconds INT,
    played_duration_seconds INT,
    completed BOOLEAN DEFAULT FALSE,
    device_type VARCHAR(50),
    ip_address VARCHAR(45),
    
    INDEX idx_user_id (user_id),
    INDEX idx_song_id (song_id),
    INDEX idx_played_at (played_at),
    INDEX idx_user_played_at (user_id, played_at),
    INDEX idx_device_type (device_type)
);

-- Sample data for testing
INSERT INTO playback_queues (user_id, volume) VALUES
(1, 80),
(2, 100);

INSERT INTO queue_items (queue_id, song_id, song_title, song_artist, position_in_queue) VALUES
(1, 1, 'Amazing Song', 'John Doe', 1),
(1, 2, 'Pop Hit', 'Jane Smith', 2),
(2, 2, 'Pop Hit', 'Jane Smith', 1);

INSERT INTO play_history (user_id, song_id, song_title, song_artist, duration_seconds, played_duration_seconds, completed, device_type) VALUES
(1, 1, 'Amazing Song', 'John Doe', 240, 240, TRUE, 'Desktop'),
(2, 2, 'Pop Hit', 'Jane Smith', 180, 120, FALSE, 'Mobile'),
(1, 2, 'Pop Hit', 'Jane Smith', 180, 180, TRUE, 'Desktop');
