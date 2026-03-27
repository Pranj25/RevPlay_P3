package com.revplay.playlistservice.repository;

import com.revplay.playlistservice.entity.PlaylistFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistFollowRepository extends JpaRepository<PlaylistFollow, Long> {
    
    Optional<PlaylistFollow> findByUserIdAndPlaylistId(Long userId, Long playlistId);
    
    List<PlaylistFollow> findByUserId(Long userId);
    
    List<PlaylistFollow> findByPlaylistId(Long playlistId);
    
    @Query("SELECT COUNT(pf) FROM PlaylistFollow pf WHERE pf.playlistId = :playlistId")
    Long countByPlaylistId(@Param("playlistId") Long playlistId);
    
    @Query("SELECT COUNT(pf) FROM PlaylistFollow pf WHERE pf.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    void deleteByUserIdAndPlaylistId(Long userId, Long playlistId);
}
