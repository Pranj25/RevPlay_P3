package com.revplay.analyticsservice.controller;

import com.revplay.analyticsservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    // Song Analytics Endpoints
    @PostMapping("/song/play")
    public ResponseEntity<Map<String, String>> recordSongPlay(
            @RequestParam Long songId,
            @RequestParam String songTitle,
            @RequestParam String songArtist,
            @RequestParam Long duration,
            @RequestParam boolean completed) {
        
        try {
            analyticsService.recordSongPlay(songId, songTitle, songArtist, duration, completed);
            return ResponseEntity.ok(Map.of("message", "Song play recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/song/like")
    public ResponseEntity<Map<String, String>> recordSongLike(
            @RequestParam Long songId,
            @RequestParam String songTitle,
            @RequestParam String songArtist) {
        
        try {
            analyticsService.recordSongLike(songId, songTitle, songArtist);
            return ResponseEntity.ok(Map.of("message", "Song like recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/song/share")
    public ResponseEntity<Map<String, String>> recordSongShare(
            @RequestParam Long songId,
            @RequestParam String songTitle,
            @RequestParam String songArtist) {
        
        try {
            analyticsService.recordSongShare(songId, songTitle, songArtist);
            return ResponseEntity.ok(Map.of("message", "Song share recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // User Analytics Endpoints
    @PostMapping("/user/activity")
    public ResponseEntity<Map<String, String>> recordUserActivity(
            @RequestParam Long userId,
            @RequestParam Long playTime,
            @RequestParam int songsPlayed,
            @RequestParam int uniqueSongs,
            @RequestParam boolean newSession) {
        
        try {
            analyticsService.recordUserActivity(userId, playTime, songsPlayed, uniqueSongs, newSession);
            return ResponseEntity.ok(Map.of("message", "User activity recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/user/like")
    public ResponseEntity<Map<String, String>> recordUserLike(@RequestParam Long userId) {
        try {
            analyticsService.recordUserLike(userId);
            return ResponseEntity.ok(Map.of("message", "User like recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/user/playlist")
    public ResponseEntity<Map<String, String>> recordPlaylistCreation(@RequestParam Long userId) {
        try {
            analyticsService.recordPlaylistCreation(userId);
            return ResponseEntity.ok(Map.of("message", "Playlist creation recorded successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Reports and Trends Endpoints
    @GetMapping("/reports/song/{songId}")
    public ResponseEntity<List<Object[]>> getSongReport(
            @PathVariable Long songId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Object[]> dailyStats = analyticsService.getDailySongStats(songId, startDate);
        return ResponseEntity.ok(dailyStats);
    }
    
    @GetMapping("/reports/user/{userId}")
    public ResponseEntity<List<Object[]>> getUserReport(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<Object[]> dailyStats = analyticsService.getDailyUserStats(userId, startDate);
        return ResponseEntity.ok(dailyStats);
    }
    
    @GetMapping("/reports/platform")
    public ResponseEntity<List<Object[]>> getPlatformReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        
        List<Object[]> dailyStats = analyticsService.getDailyPlatformStats(startDate);
        return ResponseEntity.ok(dailyStats);
    }
    
    // Trends Endpoints
    @GetMapping("/trends/top-songs")
    public ResponseEntity<List<Object[]>> getTopPlayedSongs(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> topSongs = analyticsService.getTopPlayedSongs(since, limit);
        return ResponseEntity.ok(topSongs);
    }
    
    @GetMapping("/trends/top-artists")
    public ResponseEntity<List<Object[]>> getTopArtists(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> topArtists = analyticsService.getTopArtists(since, limit);
        return ResponseEntity.ok(topArtists);
    }
    
    @GetMapping("/trends/top-genres")
    public ResponseEntity<List<Object[]>> getTopGenres(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> topGenres = analyticsService.getTopGenres(since, limit);
        return ResponseEntity.ok(topGenres);
    }
    
    @GetMapping("/trends/top-users")
    public ResponseEntity<List<Object[]>> getTopUsersByPlayTime(
            @RequestParam(defaultValue = "30") int days,
            @RequestParam(defaultValue = "10") int limit) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> topUsers = analyticsService.getTopUsersByPlayTime(since, limit);
        return ResponseEntity.ok(topUsers);
    }
    
    // Summary Statistics Endpoints
    @GetMapping("/summary/song/{songId}")
    public ResponseEntity<Map<String, Object>> getSongSummary(@PathVariable Long songId) {
        long totalPlays = analyticsService.getTotalPlaysForSong(songId);
        return ResponseEntity.ok(Map.of(
            "songId", songId,
            "totalPlays", totalPlays
        ));
    }
    
    @GetMapping("/summary/platform")
    public ResponseEntity<Map<String, Object>> getPlatformSummary(
            @RequestParam(defaultValue = "30") int days) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        
        long totalPlays = analyticsService.getTotalPlaysSince(since);
        long uniqueSongs = analyticsService.getUniqueSongsPlayedSince(since);
        long activeUsers = analyticsService.getActiveUsersSince(since);
        double avgSessionDuration = analyticsService.getAverageSessionDurationSince(since);
        
        return ResponseEntity.ok(Map.of(
            "period", days + " days",
            "totalPlays", totalPlays,
            "uniqueSongsPlayed", uniqueSongs,
            "activeUsers", activeUsers,
            "averageSessionDuration", avgSessionDuration
        ));
    }
    
    @GetMapping("/summary/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserSummary(@PathVariable Long userId) {
        long totalPlayTime = analyticsService.getTotalPlayTimeForUser(userId);
        return ResponseEntity.ok(Map.of(
            "userId", userId,
            "totalPlayTime", totalPlayTime
        ));
    }
}
