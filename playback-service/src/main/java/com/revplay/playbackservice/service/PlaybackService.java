package com.revplay.playbackservice.service;

import com.revplay.playbackservice.entity.PlayHistory;
import com.revplay.playbackservice.repository.PlayHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaybackService {
    
    @Autowired
    private PlayHistoryRepository playHistoryRepository;
    
    @Autowired
    private MediaStreamingService mediaStreamingService;
    
    public PlayHistory recordPlayback(Long userId, Long songId, String songTitle, String songArtist, 
                                  HttpServletRequest request) {
        PlayHistory playHistory = new PlayHistory(userId, songId, songTitle, songArtist);
        playHistory.setIpAddress(getClientIpAddress(request));
        playHistory.setDeviceType(detectDeviceType(request));
        
        return playHistoryRepository.save(playHistory);
    }
    
    public PlayHistory recordPlaybackWithDetails(Long userId, Long songId, String songTitle, String songArtist,
                                             Integer durationSeconds, Integer playedDurationSeconds,
                                             Boolean completed, HttpServletRequest request) {
        PlayHistory playHistory = new PlayHistory(userId, songId, songTitle, songArtist);
        playHistory.setDurationSeconds(durationSeconds);
        playHistory.setPlayedDurationSeconds(playedDurationSeconds);
        playHistory.setCompleted(completed);
        playHistory.setIpAddress(getClientIpAddress(request));
        playHistory.setDeviceType(detectDeviceType(request));
        
        return playHistoryRepository.save(playHistory);
    }
    
    public List<PlayHistory> getUserPlayHistory(Long userId) {
        return playHistoryRepository.findByUserIdOrderByPlayedAtDesc(userId);
    }
    
    public List<PlayHistory> getUserPlayHistoryInRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return playHistoryRepository.findByUserIdAndPlayedAtBetweenOrderByPlayedAtDesc(userId, startDate, endDate);
    }
    
    public List<PlayHistory> getSongPlayHistory(Long songId) {
        return playHistoryRepository.findBySongIdOrderByPlayedAtDesc(songId);
    }
    
    public Long getUserTotalPlays(Long userId) {
        return playHistoryRepository.countByUserId(userId);
    }
    
    public Long getSongTotalPlays(Long songId) {
        return playHistoryRepository.countBySongId(songId);
    }
    
    public Long getUserSongPlays(Long userId, Long songId) {
        return playHistoryRepository.countByUserIdAndSongId(userId, songId);
    }
    
    public List<Object[]> getMostPlayedSongsSince(LocalDateTime since) {
        return playHistoryRepository.findMostPlayedSongsSince(since);
    }
    
    public List<Object[]> getUserMostPlayedSongs(Long userId) {
        return playHistoryRepository.findUserMostPlayedSongs(userId);
    }
    
    public List<PlayHistory> getRecentlyPlayedByUser(Long userId, int limit) {
        return playHistoryRepository.findRecentlyPlayedByUserWithLimit(userId, limit);
    }
    
    public List<PlayHistory> getLast50Played(Long userId) {
        return playHistoryRepository.findLast50PlayedByUser(userId);
    }
    
    public void clearHistory(Long userId) {
        playHistoryRepository.deleteByUserId(userId);
    }
    
    public Long getTotalListeningTime(Long userId) {
        List<PlayHistory> history = playHistoryRepository.findByUserIdOrderByPlayedAtDesc(userId);
        return history.stream()
            .mapToLong(ph -> ph.getPlayedDurationSeconds() != null ? ph.getPlayedDurationSeconds() : 0L)
            .sum();
    }
    
    public List<Object[]> getMostPlayedSongs(Long userId) {
        return playHistoryRepository.findUserMostPlayedSongs(userId);
    }
    
    public List<Object[]> getDailyStats(Long userId) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return playHistoryRepository.findDailyPlayStats(userId, thirtyDaysAgo);
    }
    
    public List<Object[]> getDailyPlayStats(Long userId, LocalDateTime startDate) {
        return playHistoryRepository.findDailyPlayStats(userId, startDate);
    }
    
    public boolean canStreamSong(String filePath) {
        return mediaStreamingService.fileExists(filePath) && 
               mediaStreamingService.isFormatSupported(mediaStreamingService.getFileFormat(filePath));
    }
    
    public long getSongFileSize(String filePath) throws Exception {
        if (!mediaStreamingService.fileExists(filePath)) {
            throw new Exception("File not found");
        }
        return mediaStreamingService.getFileSize(filePath);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    private String detectDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown";
        }
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone")) {
            return "Mobile";
        } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        } else if (userAgent.contains("desktop") || userAgent.contains("windows") || userAgent.contains("mac")) {
            return "Desktop";
        } else {
            return "Unknown";
        }
    }
}
