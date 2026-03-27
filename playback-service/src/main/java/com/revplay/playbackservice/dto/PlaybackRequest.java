package com.revplay.playbackservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlaybackRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Song ID is required")
    private Long songId;
    
    @NotBlank(message = "Song title is required")
    private String songTitle;
    
    @NotBlank(message = "Song artist is required")
    private String songArtist;
    
    private String filePath;
    
    private Integer durationSeconds;
    
    private Integer playedDurationSeconds;
    
    private Boolean completed = false;
    
    public PlaybackRequest() {}
    
    public PlaybackRequest(Long userId, Long songId, String songTitle, String songArtist, String filePath) {
        this.userId = userId;
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.filePath = filePath;
    }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    
    public Integer getPlayedDurationSeconds() { return playedDurationSeconds; }
    public void setPlayedDurationSeconds(Integer playedDurationSeconds) { this.playedDurationSeconds = playedDurationSeconds; }
    
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
