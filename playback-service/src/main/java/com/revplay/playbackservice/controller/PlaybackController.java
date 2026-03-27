package com.revplay.playbackservice.controller;

import com.revplay.playbackservice.dto.PlaybackRequest;
import com.revplay.playbackservice.entity.PlayHistory;
import com.revplay.playbackservice.service.MediaStreamingService;
import com.revplay.playbackservice.service.PlaybackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playback")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class PlaybackController {
    
    @Autowired
    private PlaybackService playbackService;
    
    @Autowired
    private MediaStreamingService mediaStreamingService;
    
    @PostMapping("/record")
    public ResponseEntity<PlayHistory> recordPlayback(@Valid @RequestBody PlaybackRequest request,
                                                   HttpServletRequest httpRequest) {
        try {
            PlayHistory playHistory = playbackService.recordPlaybackWithDetails(
                request.getUserId(),
                request.getSongId(),
                request.getSongTitle(),
                request.getSongArtist(),
                request.getDurationSeconds(),
                request.getPlayedDurationSeconds(),
                request.getCompleted(),
                httpRequest
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(playHistory);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/stream/{songId}")
    public ResponseEntity<Resource> streamSong(@PathVariable Long songId,
                                            @RequestParam String filePath,
                                            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            if (!playbackService.canStreamSong(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            return mediaStreamingService.streamMedia(filePath, rangeHeader);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/stream/info/{songId}")
    public ResponseEntity<Map<String, Object>> getStreamInfo(@PathVariable Long songId,
                                                           @RequestParam String filePath) {
        try {
            if (!playbackService.canStreamSong(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            long fileSize = playbackService.getSongFileSize(filePath);
            String format = mediaStreamingService.getFileFormat(filePath);
            
            return ResponseEntity.ok(Map.of(
                "songId", songId,
                "filePath", filePath,
                "fileSize", fileSize,
                "format", format,
                "streamable", true
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/history/user/{userId}")
    public ResponseEntity<List<PlayHistory>> getUserPlayHistory(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "50") int limit) {
        List<PlayHistory> history = playbackService.getRecentlyPlayedByUser(userId, limit);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/history/user/{userId}/range")
    public ResponseEntity<List<PlayHistory>> getUserPlayHistoryInRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            
            List<PlayHistory> history = playbackService.getUserPlayHistoryInRange(userId, start, end);
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/history/song/{songId}")
    public ResponseEntity<List<PlayHistory>> getSongPlayHistory(@PathVariable Long songId) {
        List<PlayHistory> history = playbackService.getSongPlayHistory(songId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/stats/user/{userId}/total")
    public ResponseEntity<Map<String, Long>> getUserTotalStats(@PathVariable Long userId) {
        Long totalPlays = playbackService.getUserTotalPlays(userId);
        return ResponseEntity.ok(Map.of("totalPlays", totalPlays));
    }
    
    @GetMapping("/stats/song/{songId}/total")
    public ResponseEntity<Map<String, Long>> getSongTotalStats(@PathVariable Long songId) {
        Long totalPlays = playbackService.getSongTotalPlays(songId);
        return ResponseEntity.ok(Map.of("totalPlays", totalPlays));
    }
    
    @GetMapping("/stats/user/{userId}/song/{songId}")
    public ResponseEntity<Map<String, Long>> getUserSongStats(@PathVariable Long userId, @PathVariable Long songId) {
        Long playCount = playbackService.getUserSongPlays(userId, songId);
        return ResponseEntity.ok(Map.of("playCount", playCount));
    }
    
    @GetMapping("/stats/most-played")
    public ResponseEntity<List<Object[]>> getMostPlayedSongs(
            @RequestParam(defaultValue = "30") int days) {
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> mostPlayed = playbackService.getMostPlayedSongsSince(since);
        return ResponseEntity.ok(mostPlayed);
    }
    
    @GetMapping("/stats/user/{userId}/most-played")
    public ResponseEntity<List<Object[]>> getUserMostPlayedSongs(@PathVariable Long userId) {
        List<Object[]> mostPlayed = playbackService.getUserMostPlayedSongs(userId);
        return ResponseEntity.ok(mostPlayed);
    }
    
    @GetMapping("/stats/user/{userId}/daily")
    public ResponseEntity<List<Object[]>> getDailyPlayStats(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "30") int days) {
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Object[]> stats = playbackService.getDailyPlayStats(userId, startDate);
        return ResponseEntity.ok(stats);
    }
}
