package com.revplay.common.feign;

import com.revplay.common.dto.SongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "catalog-service", configuration = com.revplay.common.config.FeignConfig.class)
public interface CatalogServiceClient {
    
    @GetMapping("/api/songs/{id}")
    SongDto getSongById(@PathVariable("id") Long id);
    
    @GetMapping("/api/songs")
    List<SongDto> getAllSongs();
    
    @GetMapping("/api/songs/user/{userId}")
    List<SongDto> getSongsByUser(@PathVariable("userId") Long userId);
    
    @GetMapping("/api/songs/search")
    List<SongDto> searchSongs(@RequestParam("q") String query);
    
    @GetMapping("/api/songs/genre/{genre}")
    List<SongDto> getSongsByGenre(@PathVariable("genre") String genre);
    
    @GetMapping("/api/songs/artist/{artist}")
    List<SongDto> getSongsByArtist(@PathVariable("artist") String artist);
    
    @GetMapping("/api/songs/most-played")
    List<SongDto> getMostPlayedSongs();
    
    @GetMapping("/api/songs/latest")
    List<SongDto> getLatestSongs();
    
    @PutMapping("/api/songs/{id}")
    SongDto updateSong(@PathVariable("id") Long id, @RequestBody SongDto songDto);
    
    @DeleteMapping("/api/songs/{id}")
    void deleteSong(@PathVariable("id") Long id);
    
    @PostMapping("/api/songs/{id}/play")
    SongDto incrementPlayCount(@PathVariable("id") Long id);
    
    @GetMapping("/api/songs/download/{id}")
    ResponseEntity<Resource> downloadSong(@PathVariable("id") Long id);
    
    @GetMapping("/api/songs/cover/{id}")
    ResponseEntity<Resource> getCoverImage(@PathVariable("id") Long id);
    
    @GetMapping("/api/songs/exists/{id}")
    boolean songExists(@PathVariable("id") Long id);
}
