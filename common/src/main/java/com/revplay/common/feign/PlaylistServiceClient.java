package com.revplay.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "PLAYLIST-SERVICE")
public interface PlaylistServiceClient {
    
    @GetMapping("/playlists/{id}")
    Map<String, Object> getPlaylistById(@PathVariable("id") Long id);
    
    @GetMapping("/playlists/user/{userId}")
    List<Map<String, Object>> getUserPlaylists(@PathVariable("userId") Long userId);
    
    @GetMapping("/playlists/user/{userId}/public")
    List<Map<String, Object>> getUserPublicPlaylists(@PathVariable("userId") Long userId);
    
    @GetMapping("/playlists/public")
    List<Map<String, Object>> getPublicPlaylists();
    
    @GetMapping("/playlists/search")
    List<Map<String, Object>> searchPlaylists(@RequestParam("q") String query);
    
    @GetMapping("/playlists/{playlistId}/songs")
    List<Map<String, Object>> getPlaylistSongs(@PathVariable("playlistId") Long playlistId);
    
    @GetMapping("/playlists/{playlistId}/songs/{songId}/exists")
    Map<String, Boolean> checkSongInPlaylist(@PathVariable("playlistId") Long playlistId, 
                                              @PathVariable("songId") Long songId);
    
    @GetMapping("/playlists/{playlistId}/count")
    Map<String, Long> getPlaylistSongCount(@PathVariable("playlistId") Long playlistId);
    
    @PostMapping("/playlists")
    Map<String, Object> createPlaylist(@RequestBody Map<String, Object> playlistRequest);
    
    @PutMapping("/playlists/{id}")
    Map<String, Object> updatePlaylist(@PathVariable("id") Long id, 
                                      @RequestBody Map<String, Object> playlistDetails);
    
    @DeleteMapping("/playlists/{id}")
    void deletePlaylist(@PathVariable("id") Long id);
}
