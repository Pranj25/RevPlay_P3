package com.revplay.playbackservice.controller;

import com.revplay.playbackservice.entity.PlaybackQueue;
import com.revplay.playbackservice.entity.QueueItem;
import com.revplay.playbackservice.entity.PlayHistory;
import com.revplay.playbackservice.service.QueueService;
import com.revplay.playbackservice.service.PlaybackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/playback")
@Tag(name = "Advanced Playback", description = "Advanced playback control APIs")
public class EnhancedPlaybackController {
    
    @Autowired
    private QueueService queueService;
    
    @Autowired
    private PlaybackService playbackService;
    
    // Queue Management
    @GetMapping("/{userId}/queue")
    @Operation(summary = "Get user's playback queue")
    public ResponseEntity<List<QueueItem>> getQueue(@PathVariable Long userId) {
        List<QueueItem> queue = queueService.getQueueItems(userId);
        return ResponseEntity.ok(queue);
    }
    
    @PostMapping("/{userId}/queue/add")
    @Operation(summary = "Add song to queue")
    public ResponseEntity<Void> addToQueue(@PathVariable Long userId,
                                          @RequestParam Long songId,
                                          @RequestParam String songTitle,
                                          @RequestParam String songArtist) {
        queueService.addToQueue(userId, songId, songTitle, songArtist);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{userId}/queue/{position}")
    @Operation(summary = "Remove song from queue")
    public ResponseEntity<Void> removeFromQueue(@PathVariable Long userId,
                                               @PathVariable Integer position) {
        queueService.removeFromQueue(userId, position);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{userId}/queue/reorder")
    @Operation(summary = "Reorder queue items")
    public ResponseEntity<Void> reorderQueue(@PathVariable Long userId,
                                             @RequestParam Integer fromPosition,
                                             @RequestParam Integer toPosition) {
        queueService.reorderQueue(userId, fromPosition, toPosition);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{userId}/queue")
    @Operation(summary = "Clear queue")
    public ResponseEntity<Void> clearQueue(@PathVariable Long userId) {
        queueService.clearQueue(userId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/queue/shuffle")
    @Operation(summary = "Toggle shuffle mode")
    public ResponseEntity<Void> toggleShuffle(@PathVariable Long userId) {
        queueService.toggleShuffle(userId);
        return ResponseEntity.ok().build();
    }
    
    // Playback Controls
    @GetMapping("/{userId}/current")
    @Operation(summary = "Get current playback state")
    public ResponseEntity<PlaybackQueue> getCurrentPlayback(@PathVariable Long userId) {
        PlaybackQueue queue = queueService.getOrCreateQueue(userId);
        return ResponseEntity.ok(queue);
    }
    
    @PostMapping("/{userId}/play")
    @Operation(summary = "Play song")
    public ResponseEntity<Void> play(@PathVariable Long userId,
                                     @RequestParam Long songId,
                                     @RequestParam(required = false) Integer position) {
        queueService.updateQueueState(userId, songId, position != null ? position : 0, true, false);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/pause")
    @Operation(summary = "Pause playback")
    public ResponseEntity<Void> pause(@PathVariable Long userId) {
        PlaybackQueue queue = queueService.getOrCreateQueue(userId);
        queueService.updateQueueState(userId, queue.getCurrentSongId(), queue.getCurrentPositionSeconds(), false, true);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/resume")
    @Operation(summary = "Resume playback")
    public ResponseEntity<Void> resume(@PathVariable Long userId) {
        PlaybackQueue queue = queueService.getOrCreateQueue(userId);
        queueService.updateQueueState(userId, queue.getCurrentSongId(), queue.getCurrentPositionSeconds(), true, false);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/next")
    @Operation(summary = "Play next song")
    public ResponseEntity<Map<String, Object>> next(@PathVariable Long userId) {
        QueueItem nextSong = queueService.getNextSong(userId);
        if (nextSong != null) {
            queueService.updateQueueState(userId, nextSong.getSongId(), 0, true, false);
            
            Map<String, Object> response = new HashMap<>();
            response.put("songId", nextSong.getSongId());
            response.put("title", nextSong.getSongTitle());
            response.put("artist", nextSong.getSongArtist());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{userId}/previous")
    @Operation(summary = "Play previous song")
    public ResponseEntity<Map<String, Object>> previous(@PathVariable Long userId) {
        QueueItem prevSong = queueService.getPreviousSong(userId);
        if (prevSong != null) {
            queueService.updateQueueState(userId, prevSong.getSongId(), 0, true, false);
            
            Map<String, Object> response = new HashMap<>();
            response.put("songId", prevSong.getSongId());
            response.put("title", prevSong.getSongTitle());
            response.put("artist", prevSong.getSongArtist());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{userId}/seek")
    @Operation(summary = "Seek to position")
    public ResponseEntity<Void> seek(@PathVariable Long userId,
                                    @RequestParam Integer positionSeconds) {
        PlaybackQueue queue = queueService.getOrCreateQueue(userId);
        queueService.updateQueueState(userId, queue.getCurrentSongId(), positionSeconds, queue.getIsPlaying(), queue.getIsPaused());
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/volume")
    @Operation(summary = "Set volume")
    public ResponseEntity<Void> setVolume(@PathVariable Long userId,
                                         @RequestParam Integer volume) {
        queueService.setVolume(userId, volume);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/repeat")
    @Operation(summary = "Set repeat mode")
    public ResponseEntity<Void> setRepeatMode(@PathVariable Long userId,
                                             @RequestParam PlaybackQueue.RepeatMode repeatMode) {
        queueService.setRepeatMode(userId, repeatMode);
        return ResponseEntity.ok().build();
    }
    
    // Recently Played
    @GetMapping("/{userId}/recent")
    @Operation(summary = "Get recently played songs")
    public ResponseEntity<List<PlayHistory>> getRecentlyPlayed(@PathVariable Long userId,
                                                              @RequestParam(defaultValue = "50") int limit) {
        List<PlayHistory> history = playbackService.getRecentlyPlayed(userId, limit);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/{userId}/recent/last50")
    @Operation(summary = "Get last 50 played songs")
    public ResponseEntity<List<PlayHistory>> getLast50Played(@PathVariable Long userId) {
        List<PlayHistory> history = playbackService.getLast50Played(userId);
        return ResponseEntity.ok(history);
    }
    
    @DeleteMapping("/{userId}/history")
    @Operation(summary = "Clear listening history")
    public ResponseEntity<Void> clearHistory(@PathVariable Long userId) {
        playbackService.clearHistory(userId);
        return ResponseEntity.ok().build();
    }
    
    // Statistics
    @GetMapping("/{userId}/stats")
    @Operation(summary = "Get playback statistics")
    public ResponseEntity<Map<String, Object>> getPlaybackStats(@PathVariable Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // Get total listening time
        Long totalListeningTime = playbackService.getTotalListeningTime(userId);
        stats.put("totalListeningTime", totalListeningTime);
        
        // Get most played songs
        List<Object[]> mostPlayed = playbackService.getMostPlayedSongs(userId);
        stats.put("mostPlayedSongs", mostPlayed);
        
        // Get daily stats
        List<Object[]> dailyStats = playbackService.getDailyStats(userId);
        stats.put("dailyStats", dailyStats);
        
        return ResponseEntity.ok(stats);
    }
}
