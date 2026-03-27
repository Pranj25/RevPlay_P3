package com.revplay.favouriteservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LikeRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Song ID is required")
    private Long songId;
    
    @NotBlank(message = "Song title is required")
    private String songTitle;
    
    @NotBlank(message = "Song artist is required")
    private String songArtist;
    
    public LikeRequest() {}
    
    public LikeRequest(Long userId, Long songId, String songTitle, String songArtist) {
        this.userId = userId;
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
}
