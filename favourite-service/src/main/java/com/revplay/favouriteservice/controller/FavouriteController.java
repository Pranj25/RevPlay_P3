package com.revplay.favouriteservice.controller;

import com.revplay.favouriteservice.dto.LikeRequest;
import com.revplay.favouriteservice.entity.Favourite;
import com.revplay.favouriteservice.service.FavouriteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favourites")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class FavouriteController {
    
    @Autowired
    private FavouriteService favouriteService;
    
    @PostMapping("/like")
    public ResponseEntity<Favourite> likeSong(@Valid @RequestBody LikeRequest request) {
        try {
            Favourite favourite = favouriteService.likeSong(
                request.getUserId(), 
                request.getSongId(), 
                request.getSongTitle(), 
                request.getSongArtist()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(favourite);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/unlike")
    public ResponseEntity<Void> unlikeSong(@RequestParam Long userId, @RequestParam Long songId) {
        try {
            favouriteService.unlikeSong(userId, songId);
            return ResponseEntity.noContent().build();
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(@Valid @RequestBody LikeRequest request) {
        try {
            boolean wasLiked = favouriteService.isSongLiked(request.getUserId(), request.getSongId());
            
            if (wasLiked) {
                favouriteService.unlikeSong(request.getUserId(), request.getSongId());
                return ResponseEntity.ok(Map.of(
                    "liked", false,
                    "message", "Song removed from favourites"
                ));
            } else {
                Favourite favourite = favouriteService.likeSong(
                    request.getUserId(), 
                    request.getSongId(), 
                    request.getSongTitle(), 
                    request.getSongArtist()
                );
                return ResponseEntity.ok(Map.of(
                    "liked", true,
                    "message", "Song added to favourites",
                    "favourite", favourite
                ));
            }
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkIfLiked(@RequestParam Long userId, @RequestParam Long songId) {
        boolean isLiked = favouriteService.isSongLiked(userId, songId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favourite>> getUserFavourites(@PathVariable Long userId) {
        List<Favourite> favourites = favouriteService.getUserFavourites(userId);
        return ResponseEntity.ok(favourites);
    }
    
    @GetMapping("/song/{songId}")
    public ResponseEntity<List<Favourite>> getSongFavourites(@PathVariable Long songId) {
        List<Favourite> favourites = favouriteService.getSongFavourites(songId);
        return ResponseEntity.ok(favourites);
    }
    
    @GetMapping("/song/{songId}/count")
    public ResponseEntity<Map<String, Long>> getSongLikeCount(@PathVariable Long songId) {
        Long count = favouriteService.getSongLikeCount(songId);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> getUserFavouriteCount(@PathVariable Long userId) {
        Long count = favouriteService.getUserFavouriteCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @PostMapping("/batch/likes")
    public ResponseEntity<Map<Long, Long>> getMultipleSongLikeCounts(@RequestBody List<Long> songIds) {
        Map<Long, Long> likeCounts = favouriteService.getMultipleSongLikeCounts(songIds);
        return ResponseEntity.ok(likeCounts);
    }
    
    @GetMapping("/most-liked")
    public ResponseEntity<List<Object[]>> getMostLikedSongs() {
        List<Object[]> mostLiked = favouriteService.getMostLikedSongs();
        return ResponseEntity.ok(mostLiked);
    }
}
