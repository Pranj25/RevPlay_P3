package com.revplay.analyticsservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_analytics")
public class SongAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "song_id", nullable = false)
    private Long songId;
    
    @Column(name = "song_title")
    private String songTitle;
    
    @Column(name = "song_artist")
    private String songArtist;
    
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    
    @Column(name = "play_count", nullable = false)
    private Integer playCount = 0;
    
    @Column(name = "unique_listeners")
    private Integer uniqueListeners = 0;
    
    @Column(name = "total_duration")
    private Long totalDuration = 0L; // in seconds
    
    @Column(name = "avg_completion_rate")
    private Double avgCompletionRate = 0.0;
    
    @Column(name = "likes")
    private Integer likes = 0;
    
    @Column(name = "shares")
    private Integer shares = 0;
    
    @Column(name = "downloads")
    private Integer downloads = 0;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Constructors
    public SongAnalytics() {}
    
    public SongAnalytics(Long songId, String songTitle, String songArtist, LocalDateTime date) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public Integer getPlayCount() { return playCount; }
    public void setPlayCount(Integer playCount) { this.playCount = playCount; }
    
    public Integer getUniqueListeners() { return uniqueListeners; }
    public void setUniqueListeners(Integer uniqueListeners) { this.uniqueListeners = uniqueListeners; }
    
    public Long getTotalDuration() { return totalDuration; }
    public void setTotalDuration(Long totalDuration) { this.totalDuration = totalDuration; }
    
    public Double getAvgCompletionRate() { return avgCompletionRate; }
    public void setAvgCompletionRate(Double avgCompletionRate) { this.avgCompletionRate = avgCompletionRate; }
    
    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }
    
    public Integer getShares() { return shares; }
    public void setShares(Integer shares) { this.shares = shares; }
    
    public Integer getDownloads() { return downloads; }
    public void setDownloads(Integer downloads) { this.downloads = downloads; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Utility methods
    public void incrementPlayCount() {
        this.playCount++;
    }
    
    public void incrementPlayCount(int count) {
        this.playCount += count;
    }
    
    public void incrementUniqueListeners() {
        this.uniqueListeners++;
    }
    
    public void addDuration(long duration) {
        this.totalDuration += duration;
        updateAvgCompletionRate();
    }
    
    public void incrementLikes() {
        this.likes++;
    }
    
    public void incrementShares() {
        this.shares++;
    }
    
    public void incrementDownloads() {
        this.downloads++;
    }
    
    private void updateAvgCompletionRate() {
        if (playCount > 0 && totalDuration > 0) {
            this.avgCompletionRate = (double) totalDuration / (playCount * 240); // Assuming 4 min avg song
        }
    }
}
