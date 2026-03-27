package com.revplay.catalogservice.controller;

import com.revplay.catalogservice.entity.Song;
import com.revplay.catalogservice.entity.Album;
import com.revplay.catalogservice.entity.Artist;
import com.revplay.catalogservice.service.SongService;
import com.revplay.catalogservice.service.AlbumService;
import com.revplay.catalogservice.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Unified search APIs")
public class SearchController {
    
    @Autowired
    private SongService songService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private ArtistService artistService;
    
    @GetMapping("/all")
    @Operation(summary = "Search across all content types")
    public ResponseEntity<Map<String, Object>> searchAll(@RequestParam String query,
                                                          @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> results = new HashMap<>();
        
        // Search songs
        List<Song> songs = songService.searchSongs(query);
        if (songs.size() > limit) {
            songs = songs.subList(0, limit);
        }
        results.put("songs", songs);
        
        // Search albums
        List<Album> albums = albumService.searchAlbums(query);
        if (albums.size() > limit) {
            albums = albums.subList(0, limit);
        }
        results.put("albums", albums);
        
        // Search artists
        List<Artist> artists = artistService.searchArtists(query);
        if (artists.size() > limit) {
            artists = artists.subList(0, limit);
        }
        results.put("artists", artists);
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/songs")
    @Operation(summary = "Search songs")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String query,
                                                   @RequestParam(defaultValue = "20") int limit) {
        List<Song> songs = songService.searchSongs(query);
        if (songs.size() > limit) {
            songs = songs.subList(0, limit);
        }
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/albums")
    @Operation(summary = "Search albums")
    public ResponseEntity<List<Album>> searchAlbums(@RequestParam String query,
                                                    @RequestParam(defaultValue = "20") int limit) {
        List<Album> albums = albumService.searchAlbums(query);
        if (albums.size() > limit) {
            albums = albums.subList(0, limit);
        }
        return ResponseEntity.ok(albums);
    }
    
    @GetMapping("/artists")
    @Operation(summary = "Search artists")
    public ResponseEntity<List<Artist>> searchArtists(@RequestParam String query,
                                                      @RequestParam(defaultValue = "20") int limit) {
        List<Artist> artists = artistService.searchArtists(query);
        if (artists.size() > limit) {
            artists = artists.subList(0, limit);
        }
        return ResponseEntity.ok(artists);
    }
    
    @GetMapping("/browse/genre/{genre}")
    @Operation(summary = "Browse content by genre")
    public ResponseEntity<Map<String, Object>> browseByGenre(@PathVariable String genre) {
        Map<String, Object> results = new HashMap<>();
        
        List<Song> songs = songService.getSongsByGenre(genre);
        results.put("songs", songs);
        
        List<Artist> artists = artistService.getArtistsByGenre(genre);
        results.put("artists", artists);
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/trending")
    @Operation(summary = "Get trending content")
    public ResponseEntity<Map<String, Object>> getTrending(@RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> results = new HashMap<>();
        
        List<Song> mostPlayed = songService.getMostPlayedSongs();
        if (mostPlayed.size() > limit) {
            mostPlayed = mostPlayed.subList(0, limit);
        }
        results.put("mostPlayedSongs", mostPlayed);
        
        List<Song> latest = songService.getLatestSongs();
        if (latest.size() > limit) {
            latest = latest.subList(0, limit);
        }
        results.put("latestSongs", latest);
        
        List<Artist> verifiedArtists = artistService.getVerifiedArtists();
        if (verifiedArtists.size() > limit) {
            verifiedArtists = verifiedArtists.subList(0, limit);
        }
        results.put("verifiedArtists", verifiedArtists);
        
        return ResponseEntity.ok(results);
    }
}
