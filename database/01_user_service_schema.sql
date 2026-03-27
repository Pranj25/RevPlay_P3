-- User Service Database Schema
-- RevPlay Music Streaming Platform

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS revplay_user_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE revplay_user_service;

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role ENUM('LISTENER', 'ARTIST', 'ADMIN') NOT NULL DEFAULT 'LISTENER',
    is_active BOOLEAN DEFAULT TRUE,
    profile_picture_path TEXT,
    bio TEXT,
    account_stats_total_playlists INT DEFAULT 0,
    account_stats_total_favorites INT DEFAULT 0,
    account_stats_listening_time BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
);

-- Sample data for testing
INSERT INTO users (username, email, password, first_name, last_name, role) VALUES
('john_doe', 'john@example.com', '$2a$10$encrypted_password_hash', 'John', 'Doe', 'LISTENER'),
('jane_artist', 'jane@example.com', '$2a$10$encrypted_password_hash', 'Jane', 'Smith', 'ARTIST'),
('admin_user', 'admin@example.com', '$2a$10$encrypted_password_hash', 'Admin', 'User', 'ADMIN');
