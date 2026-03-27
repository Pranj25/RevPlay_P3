package com.revplay.playbackservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "play_history")
public class PlayHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "song_id", nullable = false)
    private Long songId;
    
    @Column(name = "song_title")
    private String songTitle;
    
    @Column(name = "song_artist")
    private String songArtist;
    
    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "played_duration_seconds")
    private Integer playedDurationSeconds;
    
    @Column(name = "completed")
    private Boolean completed = false;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    // Constructors
    public PlayHistory() {}
    
    public PlayHistory(Long userId, Long songId, String songTitle, String songArtist) {
        this.userId = userId;
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.playedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    
    public LocalDateTime getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDateTime playedAt) { this.playedAt = playedAt; }
    
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    
    public Integer getPlayedDurationSeconds() { return playedDurationSeconds; }
    public void setPlayedDurationSeconds(Integer playedDurationSeconds) { this.playedDurationSeconds = playedDurationSeconds; }
    
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
