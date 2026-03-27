package com.revplay.catalogservice.repository;

import com.revplay.catalogservice.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    
    List<Album> findByArtistId(Long artistId);
    
    List<Album> findByArtistIdAndIsPublicTrue(Long artistId);
    
    List<Album> findByIsPublicTrue();
    
    Optional<Album> findByIdAndIsPublicTrue(Long id);
    
    @Query("SELECT a FROM Album a WHERE a.name LIKE %:name% AND a.isPublic = true")
    List<Album> findByNameContainingAndIsPublicTrue(@Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Album a WHERE a.artistId = :artistId")
    Long countByArtistId(@Param("artistId") Long artistId);
}
