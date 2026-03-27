package com.revplay.favouriteservice.service;

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
public class FavouriteService {
    
    @Autowired
    private FavouriteRepository favouriteRepository;
    
    public Favourite likeSong(Long userId, Long songId, String songTitle, String songArtist) {
        // Check if song is already liked
        if (favouriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song is already in favourites");
        }
        
        Favourite favourite = new Favourite(userId, songId, songTitle, songArtist);
        return favouriteRepository.save(favourite);
    }
    
    public void unlikeSong(Long userId, Long songId) {
        if (!favouriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song is not in favourites");
        }
        
        favouriteRepository.deleteByUserIdAndSongId(userId, songId);
    }
    
    public boolean isSongLiked(Long userId, Long songId) {
        return favouriteRepository.existsByUserIdAndSongId(userId, songId);
    }
    
    public List<Favourite> getUserFavourites(Long userId) {
        return favouriteRepository.findByUserIdOrderByLikedAtDesc(userId);
    }
    
    public List<Favourite> getSongFavourites(Long songId) {
        return favouriteRepository.findBySongIdOrderByLikedAtDesc(songId);
    }
    
    public Long getSongLikeCount(Long songId) {
        return favouriteRepository.countBySongId(songId);
    }
    
    public Long getUserFavouriteCount(Long userId) {
        return favouriteRepository.countByUserId(userId);
    }
    
    public Map<Long, Long> getMultipleSongLikeCounts(List<Long> songIds) {
        return songIds.stream()
                .collect(Collectors.toMap(
                    songId -> songId,
                    favouriteRepository::countBySongId
                ));
    }
    
    public List<Object[]> getMostLikedSongs() {
        return favouriteRepository.findMostLikedSongs();
    }
    
    public Optional<Favourite> getFavourite(Long userId, Long songId) {
        return favouriteRepository.findByUserIdAndSongId(userId, songId);
    }
    
    public void toggleLike(Long userId, Long songId, String songTitle, String songArtist) {
        if (isSongLiked(userId, songId)) {
            unlikeSong(userId, songId);
        } else {
            likeSong(userId, songId, songTitle, songArtist);
        }
    }
}
