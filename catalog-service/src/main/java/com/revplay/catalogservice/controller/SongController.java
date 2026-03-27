package com.revplay.catalogservice.controller;

import com.revplay.catalogservice.dto.SongUploadRequest;
import com.revplay.catalogservice.entity.Song;
import com.revplay.catalogservice.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/songs")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class SongController {
    
    @Autowired
    private SongService songService;
    
    @PostMapping("/upload")
    public ResponseEntity<Song> uploadSong(
            @RequestPart("songData") @Valid SongUploadRequest songRequest,
            @RequestPart("musicFile") MultipartFile musicFile,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        
        try {
            // Create song entity
            Song song = new Song();
            song.setTitle(songRequest.getTitle());
            song.setArtist(songRequest.getArtist());
            song.setAlbum(songRequest.getAlbum());
            song.setGenre(songRequest.getGenre());
            song.setLyrics(songRequest.getLyrics());
            song.setDurationSeconds(songRequest.getDurationSeconds());
            song.setUploadedBy(songRequest.getUploadedBy());
            song.setIsPublic(songRequest.getIsPublic());
            
            // Upload song
            Song uploadedSong = songService.uploadSong(song, musicFile, coverImage);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedSong);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Optional<Song> song = songService.getSongById(id);
        return song.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Song>> getSongsByUser(@PathVariable Long userId) {
        List<Song> songs = songService.getSongsByUser(userId);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String q) {
        List<Song> songs = songService.searchSongs(q);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Song>> getSongsByGenre(@PathVariable String genre) {
        List<Song> songs = songService.getSongsByGenre(genre);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/artist/{artist}")
    public ResponseEntity<List<Song>> getSongsByArtist(@PathVariable String artist) {
        List<Song> songs = songService.getSongsByArtist(artist);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/most-played")
    public ResponseEntity<List<Song>> getMostPlayedSongs() {
        List<Song> songs = songService.getMostPlayedSongs();
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/latest")
    public ResponseEntity<List<Song>> getLatestSongs() {
        List<Song> songs = songService.getLatestSongs();
        return ResponseEntity.ok(songs);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @Valid @RequestBody Song songDetails) {
        try {
            Song updatedSong = songService.updateSong(id, songDetails);
            return ResponseEntity.ok(updatedSong);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/play")
    public ResponseEntity<Song> incrementPlayCount(@PathVariable Long id) {
        try {
            Song song = songService.incrementPlayCount(id);
            return ResponseEntity.ok(song);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadSong(@PathVariable Long id) {
        Optional<Song> songOpt = songService.getSongById(id);
        if (songOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Song song = songOpt.get();
        Resource resource = new FileSystemResource(song.getFilePath());
        
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + song.getTitle() + "." + song.getFileFormat() + "\"")
                .body(resource);
    }
    
    @GetMapping("/cover/{id}")
    public ResponseEntity<Resource> getCoverImage(@PathVariable Long id) {
        Optional<Song> songOpt = songService.getSongById(id);
        if (songOpt.isEmpty() || songOpt.get().getCoverImagePath() == null) {
            return ResponseEntity.notFound().build();
        }
        
        Song song = songOpt.get();
        Resource resource = new FileSystemResource(song.getCoverImagePath());
        
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
