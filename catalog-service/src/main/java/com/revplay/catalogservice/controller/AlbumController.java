package com.revplay.catalogservice.controller;

import com.revplay.catalogservice.entity.Album;
import com.revplay.catalogservice.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/albums")
@Tag(name = "Album Management", description = "APIs for managing albums")
public class AlbumController {
    
    @Autowired
    private AlbumService albumService;
    
    @PostMapping
    @Operation(summary = "Create a new album")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album createdAlbum = albumService.createAlbum(album);
        return ResponseEntity.ok(createdAlbum);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get album by ID")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        Optional<Album> album = albumService.getPublicAlbumById(id);
        return album.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/artist/{artistId}")
    @Operation(summary = "Get albums by artist")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable Long artistId) {
        List<Album> albums = albumService.getPublicAlbumsByArtist(artistId);
        return ResponseEntity.ok(albums);
    }
    
    @GetMapping
    @Operation(summary = "Get all public albums")
    public ResponseEntity<List<Album>> getAllPublicAlbums() {
        List<Album> albums = albumService.getAllPublicAlbums();
        return ResponseEntity.ok(albums);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search albums by name")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String name) {
        List<Album> albums = albumService.searchAlbums(name);
        return ResponseEntity.ok(albums);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update album")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        album.setId(id);
        Album updatedAlbum = albumService.updateAlbum(album);
        return ResponseEntity.ok(updatedAlbum);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete album")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        try {
            albumService.deleteAlbum(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/update-stats")
    @Operation(summary = "Update album statistics")
    public ResponseEntity<Void> updateAlbumStats(@PathVariable Long id) {
        albumService.updateAlbumStats(id);
        return ResponseEntity.ok().build();
    }
}
