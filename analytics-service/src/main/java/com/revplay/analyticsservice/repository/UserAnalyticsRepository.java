package com.revplay.analyticsservice.repository;

import com.revplay.analyticsservice.entity.UserAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnalyticsRepository extends JpaRepository<UserAnalytics, Long> {
    
    Optional<UserAnalytics> findByUserIdAndDate(Long userId, LocalDateTime date);
    
    List<UserAnalytics> findByUserIdOrderByDateDesc(Long userId);
    
    List<UserAnalytics> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT ua FROM UserAnalytics ua WHERE ua.date >= :since " +
           "ORDER BY ua.totalPlayTime DESC")
    List<UserAnalytics> findMostActiveUsersSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT ua.userId, SUM(ua.totalPlayTime) as totalPlayTime " +
           "FROM UserAnalytics ua WHERE ua.date >= :since " +
           "GROUP BY ua.userId ORDER BY totalPlayTime DESC")
    List<Object[]> findTopUsersByPlayTimeSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT ua.userId, SUM(ua.songsPlayed) as totalSongs " +
           "FROM UserAnalytics ua WHERE ua.date >= :since " +
           "GROUP BY ua.userId ORDER BY totalSongs DESC")
    List<Object[]> findTopUsersBySongsPlayedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT DATE(ua.date) as date, SUM(ua.totalPlayTime) as playTime, " +
           "SUM(ua.songsPlayed) as songsPlayed, COUNT(DISTINCT ua.userId) as activeUsers " +
           "FROM UserAnalytics ua WHERE ua.date >= :startDate " +
           "GROUP BY DATE(ua.date) ORDER BY date")
    List<Object[]> findDailyPlatformStats(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT DATE(ua.date) as date, SUM(ua.totalPlayTime) as playTime, " +
           "SUM(ua.songsPlayed) as songsPlayed " +
           "FROM UserAnalytics ua WHERE ua.userId = :userId AND ua.date >= :startDate " +
           "GROUP BY DATE(ua.date) ORDER BY date")
    List<Object[]> findDailyUserStats(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT SUM(ua.totalPlayTime) FROM UserAnalytics ua WHERE ua.userId = :userId")
    Long getTotalPlayTimeForUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(ua.totalPlayTime) FROM UserAnalytics ua WHERE ua.date >= :since")
    Long getTotalPlayTimeSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(DISTINCT ua.userId) FROM UserAnalytics ua WHERE ua.date >= :since")
    Long getActiveUsersSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT AVG(ua.avgSessionDuration) FROM UserAnalytics ua WHERE ua.date >= :since")
    Double getAverageSessionDurationSince(@Param("since") LocalDateTime since);
}
