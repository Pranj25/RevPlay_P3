package com.revplay.favouriteservice.service;

import com.revplay.common.dto.SongDto;
import com.revplay.common.dto.UserDto;
import com.revplay.common.service.ServiceCommunicationService;
import com.revplay.favouriteservice.entity.Favourite;
import com.revplay.favouriteservice.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnhancedFavouriteService {
    
    @Autowired
    private FavouriteRepository favouriteRepository;
    
    @Autowired
    private ServiceCommunicationService communicationService;
    
    public Favourite likeSong(Long userId, Long songId) {
        // Validate user exists
        if (!communicationService.userExists(userId)) {
            throw new RuntimeException("User not found");
        }
        
        // Validate song exists
        if (!communicationService.songExists(songId)) {
            throw new RuntimeException("Song not found");
        }
        
        // Check if song is already liked
        if (favouriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song is already in favourites");
        }
        
        // Get song details from catalog service
        SongDto song = communicationService.getSongById(songId);
        
        Favourite favourite = new Favourite(userId, songId, song.getTitle(), song.getArtist());
        Favourite savedFavourite = favouriteRepository.save(favourite);
        
        // Record analytics
        communicationService.recordSongLike(songId, song.getTitle(), song.getArtist());
        
        return savedFavourite;
    }
    
    public void unlikeSong(Long userId, Long songId) {
        if (!favouriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song is not in favourites");
        }
        
        favouriteRepository.deleteByUserIdAndSongId(userId, songId);
    }
    
    public Map<String, Object> getFavouriteWithDetails(Long userId, Long songId) {
        Optional<Favourite> favourite = favouriteRepository.findByUserIdAndSongId(userId, songId);
        
        if (favourite.isEmpty()) {
            return Map.of("liked", false);
        }
        
        // Get song details
        SongDto song = communicationService.getSongById(songId);
        
        return Map.of(
            "liked", true,
            "favourite", favourite.get(),
            "song", song
        );
    }
    
    public List<Map<String, Object>> getUserFavouritesWithDetails(Long userId) {
        List<Favourite> favourites = favouriteRepository.findByUserIdOrderByLikedAtDesc(userId);
        
        return favourites.stream().map(favourite -> {
            try {
                SongDto song = communicationService.getSongById(favourite.getSongId());
                return Map.of(
                    "favourite", favourite,
                    "song", song
                );
            } catch (Exception e) {
                return Map.of(
                    "favourite", favourite,
                    "song", null,
                    "error", "Song details not available"
                );
            }
        }).collect(Collectors.toList());
    }
    
    public Map<String, Object> getSongFavouriteStats(Long songId) {
        // Get like count
        Map<String, Long> likeCount = communicationService.getSongLikeCount(songId);
        
        // Get song details
        SongDto song = communicationService.getSongById(songId);
        
        // Get users who liked the song
        List<Favourite> favourites = favouriteRepository.findBySongIdOrderByLikedAtDesc(songId);
        
        return Map.of(
            "song", song,
            "likeCount", likeCount.getOrDefault("count", 0L),
            "likedBy", favourites.size(),
            "recentLikes", favourites.subList(0, Math.min(10, favourites.size()))
        );
    }
    
    public List<Map<String, Object>> getPopularSongs(int limit) {
        // Get top played songs from analytics
        List<Object[]> topSongs = communicationService.getTopPlayedSongs(30, limit);
        
        return topSongs.stream().map(songData -> {
            Long songId = (Long) songData[0];
            Long playCount = (Long) songData[1];
            
            try {
                SongDto song = communicationService.getSongById(songId);
                Map<String, Long> likeCount = communicationService.getSongLikeCount(songId);
                
                return Map.of(
                    "song", song,
                    "playCount", playCount,
                    "likeCount", likeCount.getOrDefault("count", 0L)
                );
            } catch (Exception e) {
                return Map.of(
                    "songId", songId,
                    "playCount", playCount,
                    "error", "Song details not available"
                );
            }
        }).collect(Collectors.toList());
    }
}
