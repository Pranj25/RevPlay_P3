package com.revplay.playbackservice.repository;

import com.revplay.playbackservice.entity.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
    
    List<PlayHistory> findByUserIdOrderByPlayedAtDesc(Long userId);
    
    List<PlayHistory> findByUserIdAndPlayedAtBetweenOrderByPlayedAtDesc(
            Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<PlayHistory> findBySongIdOrderByPlayedAtDesc(Long songId);
    
    @Query("SELECT COUNT(ph) FROM PlayHistory ph WHERE ph.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(ph) FROM PlayHistory ph WHERE ph.songId = :songId")
    Long countBySongId(@Param("songId") Long songId);
    
    @Query("SELECT COUNT(ph) FROM PlayHistory ph WHERE ph.userId = :userId AND ph.songId = :songId")
    Long countByUserIdAndSongId(@Param("userId") Long userId, @Param("songId") Long songId);
    
    @Query("SELECT ph.songId, COUNT(ph) as playCount FROM PlayHistory ph " +
           "WHERE ph.playedAt >= :since GROUP BY ph.songId ORDER BY playCount DESC")
    List<Object[]> findMostPlayedSongsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT ph.songId, ph.songTitle, ph.songArtist, COUNT(ph) as playCount " +
           "FROM PlayHistory ph WHERE ph.userId = :userId " +
           "GROUP BY ph.songId, ph.songTitle, ph.songArtist ORDER BY playCount DESC")
    List<Object[]> findUserMostPlayedSongs(@Param("userId") Long userId);
    
    @Query("SELECT ph FROM PlayHistory ph WHERE ph.userId = :userId " +
           "ORDER BY ph.playedAt DESC")
    List<PlayHistory> findRecentlyPlayedByUser(@Param("userId") Long userId);
    
    @Query(value = "SELECT DATE(ph.played_at) as date, COUNT(*) as play_count " +
           "FROM play_history ph WHERE ph.user_id = :userId " +
           "AND ph.played_at >= :startDate " +
           "GROUP BY DATE(ph.played_at) ORDER BY date DESC", nativeQuery = true)
    List<Object[]> findDailyPlayStats(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
}
