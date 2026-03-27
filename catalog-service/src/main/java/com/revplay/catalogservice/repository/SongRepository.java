package com.revplay.catalogservice.repository;

import com.revplay.catalogservice.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    List<Song> findByUploadedBy(Long uploadedBy);
    
    List<Song> findByIsPublic(Boolean isPublic);
    
    List<Song> findByGenre(String genre);
    
    List<Song> findByArtistId(Long artistId);
    
    List<Song> findByAlbumId(Long albumId);
    
    List<Song> findByArtistContainingIgnoreCase(String artist);
    
    List<Song> findByTitleContainingIgnoreCase(String title);
    
    List<Song> findByAlbumContainingIgnoreCase(String album);
    
    List<Song> findByArtistIdAndIsPublicTrue(Long artistId);
    
    List<Song> findByAlbumIdAndIsPublicTrue(Long albumId);
    
    @Query("SELECT s FROM Song s WHERE " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.artist) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.album) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(s.genre) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Song> searchSongs(@Param("search") String search);
    
    @Query("SELECT s FROM Song s WHERE s.isPublic = true ORDER BY s.playCount DESC")
    List<Song> findMostPlayedSongs();
    
    @Query("SELECT s FROM Song s WHERE s.isPublic = true ORDER BY s.createdAt DESC")
    List<Song> findLatestSongs();
}
