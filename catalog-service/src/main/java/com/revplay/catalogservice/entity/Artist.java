package com.revplay.catalogservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
public class Artist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "User ID is required")
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @NotBlank(message = "Artist name is required")
    @Size(max = 200, message = "Artist name must not exceed 200 characters")
    @Column(nullable = false)
    private String artistName;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "profile_image_path")
    private String profileImagePath;
    
    @Column(name = "banner_image_path")
    private String bannerImagePath;
    
    @Size(max = 100, message = "Genre must not exceed 100 characters")
    private String genre;
    
    @Column(name = "total_songs")
    private Integer totalSongs = 0;
    
    @Column(name = "total_plays")
    private Long totalPlays = 0L;
    
    @Column(name = "total_followers")
    private Integer totalFollowers = 0;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Social Media Links
    @Column(name = "instagram_url")
    private String instagramUrl;
    
    @Column(name = "twitter_url")
    private String twitterUrl;
    
    @Column(name = "youtube_url")
    private String youtubeUrl;
    
    @Column(name = "spotify_url")
    private String spotifyUrl;
    
    @Column(name = "website_url")
    private String websiteUrl;
    
    // Relationships
    @OneToMany(mappedBy = "artistId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();
    
    @OneToMany(mappedBy = "artistId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Album> albums = new HashSet<>();
    
    // Constructors
    public Artist() {}
    
    public Artist(Long userId, String artistName) {
        this.userId = userId;
        this.artistName = artistName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }
    
    public String getBannerImagePath() { return bannerImagePath; }
    public void setBannerImagePath(String bannerImagePath) { this.bannerImagePath = bannerImagePath; }
    
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    
    public Integer getTotalSongs() { return totalSongs; }
    public void setTotalSongs(Integer totalSongs) { this.totalSongs = totalSongs; }
    
    public Long getTotalPlays() { return totalPlays; }
    public void setTotalPlays(Long totalPlays) { this.totalPlays = totalPlays; }
    
    public Integer getTotalFollowers() { return totalFollowers; }
    public void setTotalFollowers(Integer totalFollowers) { this.totalFollowers = totalFollowers; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String instagramUrl) { this.instagramUrl = instagramUrl; }
    
    public String getTwitterUrl() { return twitterUrl; }
    public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }
    
    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    
    public String getSpotifyUrl() { return spotifyUrl; }
    public void setSpotifyUrl(String spotifyUrl) { this.spotifyUrl = spotifyUrl; }
    
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    
    public Set<Song> getSongs() { return songs; }
    public void setSongs(Set<Song> songs) { this.songs = songs; }
    
    public Set<Album> getAlbums() { return albums; }
    public void setAlbums(Set<Album> albums) { this.albums = albums; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
