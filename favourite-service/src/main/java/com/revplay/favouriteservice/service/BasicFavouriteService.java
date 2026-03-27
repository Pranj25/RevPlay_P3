package com.revplay.favouriteservice.service;

import com.revplay.favouriteservice.entity.Favourite;
import com.revplay.favouriteservice.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BasicFavouriteService {
    
    @Autowired
    private FavouriteRepository favouriteRepository;
    
    public Favourite addToFavourites(Long userId, Long songId, String songTitle, String songArtist) {
        // Check if already favourited
        Optional<Favourite> existing = favouriteRepository.findByUserIdAndSongId(userId, songId);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        Favourite favourite = new Favourite();
        favourite.setUserId(userId);
        favourite.setSongId(songId);
        favourite.setSongTitle(songTitle);
        favourite.setSongArtist(songArtist);
        favourite.setLikedAt(LocalDateTime.now());
        
        return favouriteRepository.save(favourite);
    }
    
    public void removeFromFavourites(Long userId, Long songId) {
        favouriteRepository.deleteByUserIdAndSongId(userId, songId);
    }
    
    public List<Favourite> getUserFavourites(Long userId) {
        return favouriteRepository.findByUserIdOrderByLikedAtDesc(userId);
    }
    
    public boolean isFavourited(Long userId, Long songId) {
        return favouriteRepository.existsByUserIdAndSongId(userId, songId);
    }
    
    public List<Favourite> getAllFavourites() {
        return favouriteRepository.findAll();
    }
    
    public long getFavouriteCount(Long songId) {
        return favouriteRepository.countBySongId(songId);
    }
}
