package com.revplay.playbackservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "queue_items")
public class QueueItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "queue_id", nullable = false)
    private Long queueId;
    
    @Column(name = "song_id", nullable = false)
    private Long songId;
    
    @Column(name = "song_title")
    private String songTitle;
    
    @Column(name = "song_artist")
    private String songArtist;
    
    @Column(name = "position_in_queue", nullable = false)
    private Integer positionInQueue;
    
    @Column(name = "added_at")
    private java.time.LocalDateTime addedAt;
    
    // Constructors
    public QueueItem() {}
    
    public QueueItem(Long queueId, Long songId, String songTitle, String songArtist, Integer positionInQueue) {
        this.queueId = queueId;
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.positionInQueue = positionInQueue;
        this.addedAt = java.time.LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getQueueId() { return queueId; }
    public void setQueueId(Long queueId) { this.queueId = queueId; }
    
    public Long getSongId() { return songId; }
    public void setSongId(Long songId) { this.songId = songId; }
    
    public String getSongTitle() { return songTitle; }
    public void setSongTitle(String songTitle) { this.songTitle = songTitle; }
    
    public String getSongArtist() { return songArtist; }
    public void setSongArtist(String songArtist) { this.songArtist = songArtist; }
    
    public Integer getPositionInQueue() { return positionInQueue; }
    public void setPositionInQueue(Integer positionInQueue) { this.positionInQueue = positionInQueue; }
    
    public java.time.LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(java.time.LocalDateTime addedAt) { this.addedAt = addedAt; }
}
