package com.revplay.playlistservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_songs")
public class PlaylistSong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;
    
    @Column(name = "song_id", nullable = false)
    private Long songId;
    
    @Column(name = "song_title")
    private String songTitle;
    
    @Column(name = "song_artist")
    private String songArtist;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @Column(name = "position_order")
    private Integer positionOrder;
    
    // Constructors
    public PlaylistSong() {}
    
    public PlaylistSong(Playlist playlist, Long songId, String songTitle, String songArtist) {
        this.playlist = playlist;
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.addedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
    
    public Integer getPositionOrder() { return positionOrder; }
    public void setPositionOrder(Integer positionOrder) { this.positionOrder = positionOrder; }
}
