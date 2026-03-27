package com.revplay.playbackservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "playback_queues")
public class PlaybackQueue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "current_song_id")
    private Long currentSongId;
    
    @Column(name = "current_position_seconds")
    private Integer currentPositionSeconds = 0;
    
    @Column(name = "is_playing")
    private Boolean isPlaying = false;
    
    @Column(name = "is_paused")
    private Boolean isPaused = false;
    
    @Column(name = "volume")
    private Integer volume = 100;
    
    @Column(name = "repeat_mode")
    @Enumerated(EnumType.STRING)
    private RepeatMode repeatMode = RepeatMode.OFF;
    
    @Column(name = "is_shuffle")
    private Boolean isShuffle = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public PlaybackQueue() {}
    
    public PlaybackQueue(Long userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getCurrentSongId() { return currentSongId; }
    public void setCurrentSongId(Long currentSongId) { this.currentSongId = currentSongId; }
    
    public Integer getCurrentPositionSeconds() { return currentPositionSeconds; }
    public void setCurrentPositionSeconds(Integer currentPositionSeconds) { this.currentPositionSeconds = currentPositionSeconds; }
    
    public Boolean getIsPlaying() { return isPlaying; }
    public void setIsPlaying(Boolean isPlaying) { this.isPlaying = isPlaying; }
    
    public Boolean getIsPaused() { return isPaused; }
    public void setIsPaused(Boolean isPaused) { this.isPaused = isPaused; }
    
    public Integer getVolume() { return volume; }
    public void setVolume(Integer volume) { this.volume = volume; }
    
    public RepeatMode getRepeatMode() { return repeatMode; }
    public void setRepeatMode(RepeatMode repeatMode) { this.repeatMode = repeatMode; }
    
    public Boolean getIsShuffle() { return isShuffle; }
    public void setIsShuffle(Boolean isShuffle) { this.isShuffle = isShuffle; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum RepeatMode {
        OFF, ONE, ALL
    }
}
