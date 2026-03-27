package com.revplay.playlistservice.repository;

import com.revplay.playlistservice.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {
    
    List<PlaylistSong> findByPlaylistIdOrderByPositionOrderAsc(Long playlistId);
    
    Optional<PlaylistSong> findByPlaylistIdAndSongId(Long playlistId, Long songId);
    
    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);
    
    @Query("SELECT COUNT(ps) FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId")
    Long countSongsInPlaylist(@Param("playlistId") Long playlistId);
    
    @Query("SELECT MAX(ps.positionOrder) FROM PlaylistSong ps WHERE ps.playlist.id = :playlistId")
    Integer findMaxPositionOrder(@Param("playlistId") Long playlistId);
}
