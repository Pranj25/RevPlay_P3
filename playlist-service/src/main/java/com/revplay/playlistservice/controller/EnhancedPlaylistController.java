package com.revplay.playlistservice.controller;

import com.revplay.playlistservice.entity.Playlist;
import com.revplay.playlistservice.entity.PlaylistSong;
import com.revplay.playlistservice.entity.PlaylistFollow;
import com.revplay.playlistservice.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/playlists")
@Tag(name = "Enhanced Playlist Management", description = "Advanced playlist management APIs")
public class EnhancedPlaylistController {
    
    @Autowired
    private PlaylistService playlistService;
    
    // Follow/Unfollow endpoints
    @PostMapping("/{playlistId}/follow")
    @Operation(summary = "Follow a playlist")
    public ResponseEntity<PlaylistFollow> followPlaylist(@PathVariable Long playlistId,
                                                         @RequestParam Long userId) {
        try {
            PlaylistFollow follow = playlistService.followPlaylist(userId, playlistId);
            return ResponseEntity.ok(follow);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{playlistId}/follow")
    @Operation(summary = "Unfollow a playlist")
    public ResponseEntity<Void> unfollowPlaylist(@PathVariable Long playlistId,
                                                  @RequestParam Long userId) {
        try {
            playlistService.unfollowPlaylist(userId, playlistId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{playlistId}/follow/status")
    @Operation(summary = "Check if user follows playlist")
    public ResponseEntity<Map<String, Boolean>> checkFollowStatus(@PathVariable Long playlistId,
                                                                 @RequestParam Long userId) {
        boolean isFollowing = playlistService.isFollowingPlaylist(userId, playlistId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{playlistId}/followers")
    @Operation(summary = "Get playlist followers")
    public ResponseEntity<List<PlaylistFollow>> getPlaylistFollowers(@PathVariable Long playlistId) {
        List<PlaylistFollow> followers = playlistService.getPlaylistFollowers(playlistId);
        return ResponseEntity.ok(followers);
    }
    
    @GetMapping("/user/{userId}/following")
    @Operation(summary = "Get playlists followed by user")
    public ResponseEntity<List<PlaylistFollow>> getUserFollowedPlaylists(@PathVariable Long userId) {
        List<PlaylistFollow> followedPlaylists = playlistService.getUserFollowedPlaylists(userId);
        return ResponseEntity.ok(followedPlaylists);
    }
    
    @GetMapping("/{playlistId}/stats")
    @Operation(summary = "Get playlist statistics")
    public ResponseEntity<Map<String, Object>> getPlaylistStats(@PathVariable Long playlistId) {
        Map<String, Object> stats = new HashMap<>();
        
        Long followerCount = playlistService.getPlaylistFollowerCount(playlistId);
        stats.put("followerCount", followerCount);
        
        Long songCount = playlistService.getPlaylistSongCount(playlistId);
        stats.put("songCount", songCount);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/user/{userId}/stats")
    @Operation(summary = "Get user playlist statistics")
    public ResponseEntity<Map<String, Object>> getUserPlaylistStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        Long followingCount = playlistService.getUserFollowingCount(userId);
        stats.put("followingCount", followingCount);
        
        List<Playlist> userPlaylists = playlistService.getUserPlaylists(userId);
        stats.put("totalPlaylists", userPlaylists.size());
        
        List<Playlist> publicPlaylists = playlistService.getPublicPlaylistsByUser(userId);
        stats.put("publicPlaylists", publicPlaylists.size());
        
        return ResponseEntity.ok(stats);
    }
    
    // Reordering functionality
    @PutMapping("/{playlistId}/reorder")
    @Operation(summary = "Reorder songs in playlist")
    public ResponseEntity<Void> reorderPlaylistSongs(@PathVariable Long playlistId,
                                                      @RequestParam Integer fromPosition,
                                                      @RequestParam Integer toPosition) {
        try {
            playlistService.reorderPlaylistSongs(playlistId, fromPosition, toPosition);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Public playlists discovery
    @GetMapping("/public/discover")
    @Operation(summary = "Discover public playlists")
    public ResponseEntity<List<Playlist>> discoverPublicPlaylists() {
        List<Playlist> publicPlaylists = playlistService.getPublicPlaylists();
        return ResponseEntity.ok(publicPlaylists);
    }
    
    @GetMapping("/public/user/{userId}")
    @Operation(summary = "Get user's public playlists")
    public ResponseEntity<List<Playlist>> getUserPublicPlaylists(@PathVariable Long userId) {
        List<Playlist> publicPlaylists = playlistService.getPublicPlaylistsByUser(userId);
        return ResponseEntity.ok(publicPlaylists);
    }
    
    @GetMapping("/public/trending")
    @Operation(summary = "Get trending public playlists (by follower count)")
    public ResponseEntity<List<Playlist>> getTrendingPlaylists() {
        List<Playlist> allPublic = playlistService.getPublicPlaylists();
        // Sort by follower count descending
        allPublic.sort((a, b) -> b.getFollowerCount().compareTo(a.getFollowerCount()));
        return ResponseEntity.ok(allPublic);
    }
    
    // Enhanced playlist details
    @GetMapping("/{playlistId}/details")
    @Operation(summary = "Get enhanced playlist details")
    public ResponseEntity<Map<String, Object>> getPlaylistDetails(@PathVariable Long playlistId,
                                                                @RequestParam(required = false) Long currentUserId) {
        Optional<Playlist> playlistOpt = playlistService.getPlaylistById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Playlist playlist = playlistOpt.get();
        Map<String, Object> details = new HashMap<>();
        
        details.put("playlist", playlist);
        
        List<PlaylistSong> songs = playlistService.getPlaylistSongs(playlistId);
        details.put("songs", songs);
        
        Long followerCount = playlistService.getPlaylistFollowerCount(playlistId);
        details.put("followerCount", followerCount);
        
        if (currentUserId != null) {
            boolean isFollowing = playlistService.isFollowingPlaylist(currentUserId, playlistId);
            details.put("isFollowing", isFollowing);
            
            boolean isOwner = playlist.getUserId().equals(currentUserId);
            details.put("isOwner", isOwner);
        }
        
        return ResponseEntity.ok(details);
    }
}
