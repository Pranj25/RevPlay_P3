package com.revplay.analyticsservice.repository;

import com.revplay.analyticsservice.entity.SongAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongAnalyticsRepository extends JpaRepository<SongAnalytics, Long> {
    
    Optional<SongAnalytics> findBySongIdAndDate(Long songId, LocalDateTime date);
    
    List<SongAnalytics> findBySongIdOrderByDateDesc(Long songId);
    
    List<SongAnalytics> findByDateBetweenOrderByDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    List<SongAnalytics> findBySongIdAndDateBetweenOrderByDateDesc(Long songId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT sa FROM SongAnalytics sa WHERE sa.date >= :since ORDER BY sa.playCount DESC")
    List<SongAnalytics> findMostPlayedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT sa.songId, SUM(sa.playCount) as totalPlays FROM SongAnalytics sa " +
           "WHERE sa.date >= :since GROUP BY sa.songId ORDER BY totalPlays DESC")
    List<Object[]> findTopPlayedSongsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT sa.songArtist, SUM(sa.playCount) as totalPlays FROM SongAnalytics sa " +
           "WHERE sa.date >= :since GROUP BY sa.songArtist ORDER BY totalPlays DESC")
    List<Object[]> findTopArtistsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT sa.genre, SUM(sa.playCount) as totalPlays FROM SongAnalytics sa " +
           "WHERE sa.date >= :since GROUP BY sa.genre ORDER BY totalPlays DESC")
    List<Object[]> findTopGenresSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT DATE(sa.date) as date, SUM(sa.playCount) as plays " +
           "FROM SongAnalytics sa WHERE sa.songId = :songId " +
           "AND sa.date >= :startDate GROUP BY DATE(sa.date) ORDER BY date")
    List<Object[]> findDailyPlayStatsForSong(@Param("songId") Long songId, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT DATE(sa.date) as date, SUM(sa.playCount) as plays " +
           "FROM SongAnalytics sa WHERE sa.date >= :startDate " +
           "GROUP BY DATE(sa.date) ORDER BY date")
    List<Object[]> findDailyPlayStats(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT SUM(sa.playCount) FROM SongAnalytics sa WHERE sa.songId = :songId")
    Long getTotalPlaysForSong(@Param("songId") Long songId);
    
    @Query("SELECT SUM(sa.playCount) FROM SongAnalytics sa WHERE sa.date >= :since")
    Long getTotalPlaysSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(DISTINCT sa.songId) FROM SongAnalytics sa WHERE sa.date >= :since")
    Long getUniqueSongsPlayedSince(@Param("since") LocalDateTime since);
}
