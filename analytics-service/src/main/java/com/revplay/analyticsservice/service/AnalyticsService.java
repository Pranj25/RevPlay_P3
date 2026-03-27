package com.revplay.analyticsservice.service;

import com.revplay.analyticsservice.entity.SongAnalytics;
import com.revplay.analyticsservice.entity.UserAnalytics;
import com.revplay.analyticsservice.repository.SongAnalyticsRepository;
import com.revplay.analyticsservice.repository.UserAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnalyticsService {
    
    @Autowired
    private SongAnalyticsRepository songAnalyticsRepository;
    
    @Autowired
    private UserAnalyticsRepository userAnalyticsRepository;
    
    // Song Analytics Methods
    public SongAnalytics recordSongPlay(Long songId, String songTitle, String songArtist, 
                                     Long duration, boolean completed) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<SongAnalytics> existing = songAnalyticsRepository.findBySongIdAndDate(songId, today);
        
        SongAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.incrementPlayCount();
            analytics.addDuration(duration);
            if (completed) {
                analytics.incrementLikes(); // Assuming completed means liked for simplicity
            }
        } else {
            analytics = new SongAnalytics(songId, songTitle, songArtist, today);
            analytics.incrementPlayCount();
            analytics.addDuration(duration);
            if (completed) {
                analytics.incrementLikes();
            }
        }
        
        return songAnalyticsRepository.save(analytics);
    }
    
    public void recordSongLike(Long songId, String songTitle, String songArtist) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<SongAnalytics> existing = songAnalyticsRepository.findBySongIdAndDate(songId, today);
        
        SongAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.incrementLikes();
        } else {
            analytics = new SongAnalytics(songId, songTitle, songArtist, today);
            analytics.incrementLikes();
        }
        
        songAnalyticsRepository.save(analytics);
    }
    
    public void recordSongShare(Long songId, String songTitle, String songArtist) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<SongAnalytics> existing = songAnalyticsRepository.findBySongIdAndDate(songId, today);
        
        SongAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.incrementShares();
        } else {
            analytics = new SongAnalytics(songId, songTitle, songArtist, today);
            analytics.incrementShares();
        }
        
        songAnalyticsRepository.save(analytics);
    }
    
    // User Analytics Methods
    public UserAnalytics recordUserActivity(Long userId, Long playTime, int songsPlayed, 
                                         int uniqueSongs, boolean newSession) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<UserAnalytics> existing = userAnalyticsRepository.findByUserIdAndDate(userId, today);
        
        UserAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.addPlayTime(playTime);
            analytics.incrementSongsPlayed();
            if (newSession) {
                analytics.incrementSessionsCount();
            }
        } else {
            analytics = new UserAnalytics(userId, today);
            analytics.addPlayTime(playTime);
            analytics.incrementSongsPlayed();
            if (newSession) {
                analytics.incrementSessionsCount();
            }
        }
        
        return userAnalyticsRepository.save(analytics);
    }
    
    public void recordUserLike(Long userId) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<UserAnalytics> existing = userAnalyticsRepository.findByUserIdAndDate(userId, today);
        
        UserAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.incrementSongsLiked();
        } else {
            analytics = new UserAnalytics(userId, today);
            analytics.incrementSongsLiked();
        }
        
        userAnalyticsRepository.save(analytics);
    }
    
    public void recordPlaylistCreation(Long userId) {
        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        Optional<UserAnalytics> existing = userAnalyticsRepository.findByUserIdAndDate(userId, today);
        
        UserAnalytics analytics;
        if (existing.isPresent()) {
            analytics = existing.get();
            analytics.incrementPlaylistsCreated();
        } else {
            analytics = new UserAnalytics(userId, today);
            analytics.incrementPlaylistsCreated();
        }
        
        userAnalyticsRepository.save(analytics);
    }
    
    // Report Methods
    public List<SongAnalytics> getSongAnalytics(Long songId, LocalDateTime startDate, LocalDateTime endDate) {
        return songAnalyticsRepository.findBySongIdAndDateBetweenOrderByDateDesc(songId, startDate, endDate);
    }
    
    public List<UserAnalytics> getUserAnalytics(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return userAnalyticsRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
    }
    
    public List<Object[]> getTopPlayedSongs(LocalDateTime since, int limit) {
        List<Object[]> results = songAnalyticsRepository.findTopPlayedSongsSince(since);
        return results.size() > limit ? results.subList(0, limit) : results;
    }
    
    public List<Object[]> getTopArtists(LocalDateTime since, int limit) {
        List<Object[]> results = songAnalyticsRepository.findTopArtistsSince(since);
        return results.size() > limit ? results.subList(0, limit) : results;
    }
    
    public List<Object[]> getTopGenres(LocalDateTime since, int limit) {
        List<Object[]> results = songAnalyticsRepository.findTopGenresSince(since);
        return results.size() > limit ? results.subList(0, limit) : results;
    }
    
    public List<Object[]> getTopUsersByPlayTime(LocalDateTime since, int limit) {
        List<Object[]> results = userAnalyticsRepository.findTopUsersByPlayTimeSince(since);
        return results.size() > limit ? results.subList(0, limit) : results;
    }
    
    public List<Object[]> getDailyPlatformStats(LocalDateTime startDate) {
        return userAnalyticsRepository.findDailyPlatformStats(startDate);
    }
    
    public List<Object[]> getDailyUserStats(Long userId, LocalDateTime startDate) {
        return userAnalyticsRepository.findDailyUserStats(userId, startDate);
    }
    
    public List<Object[]> getDailySongStats(Long songId, LocalDateTime startDate) {
        return songAnalyticsRepository.findDailyPlayStatsForSong(songId, startDate);
    }
    
    // Summary Statistics
    public long getTotalPlaysForSong(Long songId) {
        return songAnalyticsRepository.getTotalPlaysForSong(songId);
    }
    
    public long getTotalPlaysSince(LocalDateTime since) {
        return songAnalyticsRepository.getTotalPlaysSince(since);
    }
    
    public long getUniqueSongsPlayedSince(LocalDateTime since) {
        return songAnalyticsRepository.getUniqueSongsPlayedSince(since);
    }
    
    public long getTotalPlayTimeForUser(Long userId) {
        return userAnalyticsRepository.getTotalPlayTimeForUser(userId);
    }
    
    public long getActiveUsersSince(LocalDateTime since) {
        return userAnalyticsRepository.getActiveUsersSince(since);
    }
    
    public double getAverageSessionDurationSince(LocalDateTime since) {
        return userAnalyticsRepository.getAverageSessionDurationSince(since);
    }
}
