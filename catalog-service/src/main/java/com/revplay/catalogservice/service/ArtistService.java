package com.revplay.catalogservice.service;

import com.revplay.catalogservice.entity.Artist;
import com.revplay.catalogservice.entity.Song;
import com.revplay.catalogservice.repository.ArtistRepository;
import com.revplay.catalogservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }
    
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }
    
    public Optional<Artist> getArtistByUserId(Long userId) {
        return artistRepository.findByUserId(userId);
    }
    
    public List<Artist> getAllActiveArtists() {
        return artistRepository.findByIsActiveTrue();
    }
    
    public List<Artist> getVerifiedArtists() {
        return artistRepository.findByIsActiveTrueAndIsVerifiedTrue();
    }
    
    public List<Artist> searchArtists(String name) {
        return artistRepository.findByArtistNameContainingAndIsActiveTrue(name);
    }
    
    public List<Artist> getArtistsByGenre(String genre) {
        return artistRepository.findByGenreContainingAndIsActiveTrue(genre);
    }
    
    public Artist updateArtist(Artist artist) {
        return artistRepository.save(artist);
    }
    
    public void deleteArtist(Long id) {
        artistRepository.deleteById(id);
    }
    
    public void updateArtistStats(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            List<Song> songs = songRepository.findByArtistId(artistId);
            artist.setTotalSongs(songs.size());
            artist.setTotalPlays(songs.stream()
                .mapToLong(song -> song.getPlayCount() != null ? song.getPlayCount() : 0L)
                .sum());
            artistRepository.save(artist);
        }
    }
    
    public void verifyArtist(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setIsVerified(true);
            artistRepository.save(artist);
        }
    }
    
    public void deactivateArtist(Long artistId) {
        Optional<Artist> artistOpt = artistRepository.findById(artistId);
        if (artistOpt.isPresent()) {
            Artist artist = artistOpt.get();
            artist.setIsActive(false);
            artistRepository.save(artist);
        }
    }
}
