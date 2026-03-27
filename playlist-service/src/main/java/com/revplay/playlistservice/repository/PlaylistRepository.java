package com.revplay.playlistservice.repository;

import com.revplay.playlistservice.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    
    List<Playlist> findByUserId(Long userId);
    
    List<Playlist> findByUserIdAndIsPublic(Long userId, Boolean isPublic);
    
    List<Playlist> findByIsPublic(Boolean isPublic);
    
    @Query("SELECT p FROM Playlist p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Playlist> searchPlaylists(@Param("search") String search);
    
    @Query("SELECT p FROM Playlist p WHERE p.isPublic = true ORDER BY p.createdAt DESC")
    List<Playlist> findPublicPlaylists();
    
    boolean existsByNameAndUserId(String name, Long userId);
}
