package com.revplay.playlistservice.controller;

import com.revplay.playlistservice.dto.AddSongRequest;
import com.revplay.playlistservice.dto.PlaylistCreateRequest;
import com.revplay.playlistservice.entity.Playlist;
import com.revplay.playlistservice.entity.PlaylistSong;
import com.revplay.playlistservice.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/playlists")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PlaylistController {
    
    @Autowired
    private PlaylistService playlistService;
    
    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@Valid @RequestBody PlaylistCreateRequest request) {
        try {
            Playlist playlist = new Playlist();
            playlist.setName(request.getName());
            playlist.setDescription(request.getDescription());
            playlist.setUserId(request.getUserId());
            playlist.setIsPublic(request.getIsPublic());
            playlist.setCoverImagePath(request.getCoverImagePath());
            
            Playlist createdPlaylist = playlistService.createPlaylist(playlist);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlaylist);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        return playlist.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylists(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.getUserPlaylists(userId);
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/user/{userId}/public")
    public ResponseEntity<List<Playlist>> getUserPublicPlaylists(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.getUserPublicPlaylists(userId);
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/public")
    public ResponseEntity<List<Playlist>> getPublicPlaylists() {
        List<Playlist> playlists = playlistService.getPublicPlaylists();
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Playlist>> searchPlaylists(@RequestParam String q) {
        List<Playlist> playlists = playlistService.searchPlaylists(q);
        return ResponseEntity.ok(playlists);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable Long id, @Valid @RequestBody Playlist playlistDetails) {
        try {
            Playlist updatedPlaylist = playlistService.updatePlaylist(id, playlistDetails);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        try {
            playlistService.deletePlaylist(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<PlaylistSong> addSongToPlaylist(
            @PathVariable Long playlistId, 
            @Valid @RequestBody AddSongRequest request) {
        
        try {
            PlaylistSong playlistSong = playlistService.addSongToPlaylist(
                playlistId, 
                request.getSongId(), 
                request.getSongTitle(), 
                request.getSongArtist()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(playlistSong);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        
        try {
            playlistService.removeSongFromPlaylist(playlistId, songId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<PlaylistSong>> getPlaylistSongs(@PathVariable Long playlistId) {
        List<PlaylistSong> songs = playlistService.getPlaylistSongs(playlistId);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/{playlistId}/songs/{songId}/exists")
    public ResponseEntity<Map<String, Boolean>> checkSongInPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        
        boolean exists = playlistService.isSongInPlaylist(playlistId, songId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    
    @GetMapping("/{playlistId}/count")
    public ResponseEntity<Map<String, Long>> getPlaylistSongCount(@PathVariable Long playlistId) {
        Long count = playlistService.getPlaylistSongCount(playlistId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
