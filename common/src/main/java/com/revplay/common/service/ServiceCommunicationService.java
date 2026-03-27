package com.revplay.common.service;

import com.revplay.common.dto.SongDto;
import com.revplay.common.dto.UserDto;
import com.revplay.common.feign.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ServiceCommunicationService {
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Autowired
    private CatalogServiceClient catalogServiceClient;
    
    @Autowired
    private FavouriteServiceClient favouriteServiceClient;
    
    @Autowired
    private PlaylistServiceClient playlistServiceClient;
    
    @Autowired
    private AnalyticsServiceClient analyticsServiceClient;
    
    // User Service Communication
    public UserDto getUserById(Long userId) {
        try {
            return userServiceClient.getUserById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user: " + e.getMessage());
        }
    }
    
    public UserDto getUserByUsername(String username) {
        try {
            return userServiceClient.getUserByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by username: " + e.getMessage());
        }
    }
    
    public boolean userExists(Long userId) {
        try {
            return userServiceClient.userExists(userId);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Catalog Service Communication
    public SongDto getSongById(Long songId) {
        try {
            return catalogServiceClient.getSongById(songId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch song: " + e.getMessage());
        }
    }
    
    public List<SongDto> getSongsByUser(Long userId) {
        try {
            return catalogServiceClient.getSongsByUser(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user songs: " + e.getMessage());
        }
    }
    
    public boolean songExists(Long songId) {
        try {
            return catalogServiceClient.songExists(songId);
        } catch (Exception e) {
            return false;
        }
    }
    
    public SongDto incrementPlayCount(Long songId) {
        try {
            return catalogServiceClient.incrementPlayCount(songId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to increment play count: " + e.getMessage());
        }
    }
    
    // Favourite Service Communication
    public Map<String, Long> getSongLikeCount(Long songId) {
        try {
            return favouriteServiceClient.getSongLikeCount(songId);
        } catch (Exception e) {
            return Map.of("count", 0L);
        }
    }
    
    public boolean isSongLiked(Long userId, Long songId) {
        try {
            Map<String, Boolean> response = favouriteServiceClient.checkIfLiked(userId, songId);
            return response.getOrDefault("liked", false);
        } catch (Exception e) {
            return false;
        }
    }
    
    public Map<Long, Long> getMultipleSongLikeCounts(List<Long> songIds) {
        try {
            return favouriteServiceClient.getMultipleSongLikeCounts(songIds);
        } catch (Exception e) {
            return Map.of();
        }
    }
    
    // Playlist Service Communication
    public List<Map<String, Object>> getUserPlaylists(Long userId) {
        try {
            return playlistServiceClient.getUserPlaylists(userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user playlists: " + e.getMessage());
        }
    }
    
    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        try {
            Map<String, Boolean> response = playlistServiceClient.checkSongInPlaylist(playlistId, songId);
            return response.getOrDefault("exists", false);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Analytics Service Communication
    public void recordSongPlay(Long songId, String songTitle, String songArtist, Long duration, boolean completed) {
        try {
            analyticsServiceClient.recordSongPlay(songId, songTitle, songArtist, duration, completed);
        } catch (Exception e) {
            // Log error but don't fail the operation
            System.err.println("Failed to record song play: " + e.getMessage());
        }
    }
    
    public void recordSongLike(Long songId, String songTitle, String songArtist) {
        try {
            analyticsServiceClient.recordSongLike(songId, songTitle, songArtist);
        } catch (Exception e) {
            System.err.println("Failed to record song like: " + e.getMessage());
        }
    }
    
    public void recordUserActivity(Long userId, Long playTime, int songsPlayed, int uniqueSongs, boolean newSession) {
        try {
            analyticsServiceClient.recordUserActivity(userId, playTime, songsPlayed, uniqueSongs, newSession);
        } catch (Exception e) {
            System.err.println("Failed to record user activity: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getSongSummary(Long songId) {
        try {
            return analyticsServiceClient.getSongSummary(songId);
        } catch (Exception e) {
            return Map.of("error", "Failed to fetch song summary");
        }
    }
    
    public List<Object[]> getTopPlayedSongs(int days, int limit) {
        try {
            return analyticsServiceClient.getTopPlayedSongs(days, limit);
        } catch (Exception e) {
            return List.of();
        }
    }
}
