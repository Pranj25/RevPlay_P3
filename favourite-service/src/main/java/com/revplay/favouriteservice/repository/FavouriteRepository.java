package com.revplay.favouriteservice.repository;

import com.revplay.favouriteservice.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    
    Optional<Favourite> findByUserIdAndSongId(Long userId, Long songId);
    
    List<Favourite> findByUserIdOrderByLikedAtDesc(Long userId);
    
    List<Favourite> findBySongIdOrderByLikedAtDesc(Long songId);
    
    void deleteByUserIdAndSongId(Long userId, Long songId);
    
    @Query("SELECT COUNT(f) FROM Favourite f WHERE f.songId = :songId")
    Long countBySongId(@Param("songId") Long songId);
    
    @Query("SELECT COUNT(f) FROM Favourite f WHERE f.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT f.songId, COUNT(f) as likeCount FROM Favourite f GROUP BY f.songId ORDER BY likeCount DESC")
    List<Object[]> findMostLikedSongs();
    
    boolean existsByUserIdAndSongId(Long userId, Long songId);
}
