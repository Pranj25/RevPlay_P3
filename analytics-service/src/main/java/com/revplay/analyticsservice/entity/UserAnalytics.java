package com.revplay.analyticsservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_analytics")
public class UserAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "total_play_time")
    private Long totalPlayTime = 0L; // in seconds
    
    @Column(name = "songs_played")
    private Integer songsPlayed = 0;
    
    @Column(name = "unique_songs_played")
    private Integer uniqueSongsPlayed = 0;
    
    @Column(name = "sessions_count")
    private Integer sessionsCount = 0;
    
    @Column(name = "avg_session_duration")
    private Double avgSessionDuration = 0.0;
    
    @Column(name = "songs_liked")
    private Integer songsLiked = 0;
    
    @Column(name = "playlists_created")
    private Integer playlistsCreated = 0;
    
    @Column(name = "songs_shared")
    private Integer songsShared = 0;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserAnalytics() {}
    
    public UserAnalytics(Long userId, LocalDateTime date) {
        this.userId = userId;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public Long getTotalPlayTime() { return totalPlayTime; }
    public void setTotalPlayTime(Long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
    
    public Integer getSongsPlayed() { return songsPlayed; }
    public void setSongsPlayed(Integer songsPlayed) { this.songsPlayed = songsPlayed; }
    
    public Integer getUniqueSongsPlayed() { return uniqueSongsPlayed; }
    public void setUniqueSongsPlayed(Integer uniqueSongsPlayed) { this.uniqueSongsPlayed = uniqueSongsPlayed; }
    
    public Integer getSessionsCount() { return sessionsCount; }
    public void setSessionsCount(Integer sessionsCount) { this.sessionsCount = sessionsCount; }
    
    public Double getAvgSessionDuration() { return avgSessionDuration; }
    public void setAvgSessionDuration(Double avgSessionDuration) { this.avgSessionDuration = avgSessionDuration; }
    
    public Integer getSongsLiked() { return songsLiked; }
    public void setSongsLiked(Integer songsLiked) { this.songsLiked = songsLiked; }
    
    public Integer getPlaylistsCreated() { return playlistsCreated; }
    public void setPlaylistsCreated(Integer playlistsCreated) { this.playlistsCreated = playlistsCreated; }
    
    public Integer getSongsShared() { return songsShared; }
    public void setSongsShared(Integer songsShared) { this.songsShared = songsShared; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Utility methods
    public void addPlayTime(long duration) {
        this.totalPlayTime += duration;
    }
    
    public void incrementSongsPlayed() {
        this.songsPlayed++;
    }
    
    public void incrementUniqueSongsPlayed() {
        this.uniqueSongsPlayed++;
    }
    
    public void incrementSessionsCount() {
        this.sessionsCount++;
        updateAvgSessionDuration();
    }
    
    public void incrementSongsLiked() {
        this.songsLiked++;
    }
    
    public void incrementPlaylistsCreated() {
        this.playlistsCreated++;
    }
    
    public void incrementSongsShared() {
        this.songsShared++;
    }
    
    private void updateAvgSessionDuration() {
        if (sessionsCount > 0 && totalPlayTime > 0) {
            this.avgSessionDuration = (double) totalPlayTime / sessionsCount;
        }
    }
}
