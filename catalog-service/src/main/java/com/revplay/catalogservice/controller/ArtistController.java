package com.revplay.catalogservice.controller;

import com.revplay.catalogservice.entity.Artist;
import com.revplay.catalogservice.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
@Tag(name = "Artist Management", description = "APIs for managing artists")
public class ArtistController {
    
    @Autowired
    private ArtistService artistService;
    
    @PostMapping
    @Operation(summary = "Create a new artist profile")
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        Artist createdArtist = artistService.createArtist(artist);
        return ResponseEntity.ok(createdArtist);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        Optional<Artist> artist = artistService.getArtistById(id);
        return artist.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get artist by user ID")
    public ResponseEntity<Artist> getArtistByUserId(@PathVariable Long userId) {
        Optional<Artist> artist = artistService.getArtistByUserId(userId);
        return artist.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all active artists")
    public ResponseEntity<List<Artist>> getAllActiveArtists() {
        List<Artist> artists = artistService.getAllActiveArtists();
        return ResponseEntity.ok(artists);
    }
    
    @GetMapping("/verified")
    @Operation(summary = "Get verified artists")
    public ResponseEntity<List<Artist>> getVerifiedArtists() {
        List<Artist> artists = artistService.getVerifiedArtists();
        return ResponseEntity.ok(artists);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search artists by name")
    public ResponseEntity<List<Artist>> searchArtists(@RequestParam String name) {
        List<Artist> artists = artistService.searchArtists(name);
        return ResponseEntity.ok(artists);
    }
    
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get artists by genre")
    public ResponseEntity<List<Artist>> getArtistsByGenre(@PathVariable String genre) {
        List<Artist> artists = artistService.getArtistsByGenre(genre);
        return ResponseEntity.ok(artists);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update artist profile")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artist) {
        artist.setId(id);
        Artist updatedArtist = artistService.updateArtist(artist);
        return ResponseEntity.ok(updatedArtist);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete artist")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify artist")
    public ResponseEntity<Void> verifyArtist(@PathVariable Long id) {
        artistService.verifyArtist(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate artist")
    public ResponseEntity<Void> deactivateArtist(@PathVariable Long id) {
        artistService.deactivateArtist(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/update-stats")
    @Operation(summary = "Update artist statistics")
    public ResponseEntity<Void> updateArtistStats(@PathVariable Long id) {
        artistService.updateArtistStats(id);
        return ResponseEntity.ok().build();
    }
}
