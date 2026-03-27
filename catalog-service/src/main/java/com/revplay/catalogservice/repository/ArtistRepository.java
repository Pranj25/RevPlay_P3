package com.revplay.catalogservice.repository;

import com.revplay.catalogservice.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
    Optional<Artist> findByUserId(Long userId);
    
    List<Artist> findByIsActiveTrue();
    
    List<Artist> findByIsActiveTrueAndIsVerifiedTrue();
    
    List<Artist> findByGenreContainingAndIsActiveTrue(String genre);
    
    @Query("SELECT a FROM Artist a WHERE a.artistName LIKE %:name% AND a.isActive = true")
    List<Artist> findByArtistNameContainingAndIsActiveTrue(@Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Artist a WHERE a.isActive = true")
    Long countActiveArtists();
    
    @Query("SELECT COUNT(a) FROM Artist a WHERE a.isActive = true AND a.isVerified = true")
    Long countVerifiedArtists();
}
