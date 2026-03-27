package com.revplay.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ANALYTICS-SERVICE")
public interface AnalyticsServiceClient {
    
    // Song Analytics
    @PostMapping("/analytics/song/play")
    Map<String, String> recordSongPlay(@RequestParam("songId") Long songId,
                                     @RequestParam("songTitle") String songTitle,
                                     @RequestParam("songArtist") String songArtist,
                                     @RequestParam("duration") Long duration,
                                     @RequestParam("completed") boolean completed);
    
    @PostMapping("/analytics/song/like")
    Map<String, String> recordSongLike(@RequestParam("songId") Long songId,
                                       @RequestParam("songTitle") String songTitle,
                                       @RequestParam("songArtist") String songArtist);
    
    @PostMapping("/analytics/song/share")
    Map<String, String> recordSongShare(@RequestParam("songId") Long songId,
                                       @RequestParam("songTitle") String songTitle,
                                       @RequestParam("songArtist") String songArtist);
    
    // User Analytics
    @PostMapping("/analytics/user/activity")
    Map<String, String> recordUserActivity(@RequestParam("userId") Long userId,
                                          @RequestParam("playTime") Long playTime,
                                          @RequestParam("songsPlayed") int songsPlayed,
                                          @RequestParam("uniqueSongs") int uniqueSongs,
                                          @RequestParam("newSession") boolean newSession);
    
    @PostMapping("/analytics/user/like")
    Map<String, String> recordUserLike(@RequestParam("userId") Long userId);
    
    @PostMapping("/analytics/user/playlist")
    Map<String, String> recordPlaylistCreation(@RequestParam("userId") Long userId);
    
    // Reports and Trends
    @GetMapping("/analytics/summary/song/{songId}")
    Map<String, Object> getSongSummary(@PathVariable("songId") Long songId);
    
    @GetMapping("/analytics/summary/user/{userId}")
    Map<String, Object> getUserSummary(@PathVariable("userId") Long userId);
    
    @GetMapping("/analytics/trends/top-songs")
    List<Object[]> getTopPlayedSongs(@RequestParam(defaultValue = "30") int days,
                                    @RequestParam(defaultValue = "10") int limit);
    
    @GetMapping("/analytics/trends/top-artists")
    List<Object[]> getTopArtists(@RequestParam(defaultValue = "30") int days,
                                 @RequestParam(defaultValue = "10") int limit);
    
    @GetMapping("/analytics/reports/platform")
    List<Object[]> getPlatformReport(@RequestParam("startDate") String startDate);
}
