package com.revplay.playlistservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_follows", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "playlist_id"}))
public class PlaylistFollow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;
    
    @Column(name = "followed_at", nullable = false)
    private LocalDateTime followedAt;
    
    // Constructors
    public PlaylistFollow() {}
    
    public PlaylistFollow(Long userId, Long playlistId) {
        this.userId = userId;
        this.playlistId = playlistId;
        this.followedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getPlaylistId() { return playlistId; }
    public void setPlaylistId(Long playlistId) { this.playlistId = playlistId; }
    
    public LocalDateTime getFollowedAt() { return followedAt; }
    public void setFollowedAt(LocalDateTime followedAt) { this.followedAt = followedAt; }
}
