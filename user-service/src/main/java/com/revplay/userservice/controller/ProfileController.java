package com.revplay.userservice.controller;

import com.revplay.userservice.entity.User;
import com.revplay.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Management", description = "APIs for managing user profiles")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile by ID")
    public ResponseEntity<User> getProfile(@PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{userId}/display-name")
    @Operation(summary = "Update display name")
    public ResponseEntity<User> updateDisplayName(@PathVariable Long userId, 
                                                 @RequestBody String displayName) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFirstName(displayName);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{userId}/bio")
    @Operation(summary = "Update bio")
    public ResponseEntity<User> updateBio(@PathVariable Long userId, 
                                         @RequestBody String bio) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setBio(bio);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{userId}/profile-picture")
    @Operation(summary = "Update profile picture")
    public ResponseEntity<User> updateProfilePicture(@PathVariable Long userId, 
                                                    @RequestBody String profilePicturePath) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setProfilePicturePath(profilePicturePath);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{userId}/stats")
    @Operation(summary = "Get user account statistics")
    public ResponseEntity<Object> getUserStats(@PathVariable Long userId) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserStats stats = new UserStats(
                user.getTotalPlaylists(),
                user.getTotalFavorites(),
                user.getListeningTime()
            );
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{userId}/stats/playlists/increment")
    @Operation(summary = "Increment playlist count")
    public ResponseEntity<Void> incrementPlaylistCount(@PathVariable Long userId) {
        userService.updatePlaylistCount(userId, 1);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/stats/favorites/increment")
    @Operation(summary = "Increment favorites count")
    public ResponseEntity<Void> incrementFavoritesCount(@PathVariable Long userId) {
        userService.updateFavoritesCount(userId, 1);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/stats/favorites/decrement")
    @Operation(summary = "Decrement favorites count")
    public ResponseEntity<Void> decrementFavoritesCount(@PathVariable Long userId) {
        userService.updateFavoritesCount(userId, -1);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/stats/listening-time/add")
    @Operation(summary = "Add listening time")
    public ResponseEntity<Void> addListeningTime(@PathVariable Long userId, 
                                                  @RequestParam Long seconds) {
        userService.updateListeningTime(userId, seconds);
        return ResponseEntity.ok().build();
    }
    
    // DTO for user stats
    public static class UserStats {
        private Integer totalPlaylists;
        private Integer totalFavorites;
        private Long listeningTime;
        
        public UserStats(Integer totalPlaylists, Integer totalFavorites, Long listeningTime) {
            this.totalPlaylists = totalPlaylists;
            this.totalFavorites = totalFavorites;
            this.listeningTime = listeningTime;
        }
        
        // Getters and setters
        public Integer getTotalPlaylists() { return totalPlaylists; }
        public void setTotalPlaylists(Integer totalPlaylists) { this.totalPlaylists = totalPlaylists; }
        
        public Integer getTotalFavorites() { return totalFavorites; }
        public void setTotalFavorites(Integer totalFavorites) { this.totalFavorites = totalFavorites; }
        
        public Long getListeningTime() { return listeningTime; }
        public void setListeningTime(Long listeningTime) { this.listeningTime = listeningTime; }
    }
}
