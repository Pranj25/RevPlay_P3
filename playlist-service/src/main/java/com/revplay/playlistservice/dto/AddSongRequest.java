package com.revplay.playlistservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddSongRequest {
    
    @NotNull(message = "Song ID is required")
    private Long songId;
    
    @NotBlank(message = "Song title is required")
    private String songTitle;
    
    @NotBlank(message = "Song artist is required")
    private String songArtist;
    
    public AddSongRequest() {}
    
    public AddSongRequest(Long songId, String songTitle, String songArtist) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
    }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
}
