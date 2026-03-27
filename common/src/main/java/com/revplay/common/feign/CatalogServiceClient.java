package com.revplay.common.feign;

import com.revplay.common.dto.SongDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "CATALOG-SERVICE")
public interface CatalogServiceClient {
    
    @GetMapping("/songs/{id}")
    SongDto getSongById(@PathVariable("id") Long id);
    
    @GetMapping("/songs")
    List<SongDto> getAllSongs();
    
    @GetMapping("/songs/user/{userId}")
    List<SongDto> getSongsByUser(@PathVariable("userId") Long userId);
    
    @GetMapping("/songs/search")
    List<SongDto> searchSongs(@RequestParam("q") String query);
    
    @GetMapping("/songs/genre/{genre}")
    List<SongDto> getSongsByGenre(@PathVariable("genre") String genre);
    
    @GetMapping("/songs/artist/{artist}")
    List<SongDto> getSongsByArtist(@PathVariable("artist") String artist);
    
    @GetMapping("/songs/most-played")
    List<SongDto> getMostPlayedSongs();
    
    @GetMapping("/songs/latest")
    List<SongDto> getLatestSongs();
    
    @PutMapping("/songs/{id}")
    SongDto updateSong(@PathVariable("id") Long id, @RequestBody SongDto songDto);
    
    @DeleteMapping("/songs/{id}")
    void deleteSong(@PathVariable("id") Long id);
    
    @PostMapping("/songs/{id}/play")
    SongDto incrementPlayCount(@PathVariable("id") Long id);
    
    @GetMapping("/songs/download/{id}")
    ResponseEntity<Resource> downloadSong(@PathVariable("id") Long id);
    
    @GetMapping("/songs/cover/{id}")
    ResponseEntity<Resource> getCoverImage(@PathVariable("id") Long id);
    
    @GetMapping("/songs/exists/{id}")
    boolean songExists(@PathVariable("id") Long id);
}
