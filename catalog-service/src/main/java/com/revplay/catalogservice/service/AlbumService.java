package com.revplay.catalogservice.service;

import com.revplay.catalogservice.entity.Album;
import com.revplay.catalogservice.entity.Song;
import com.revplay.catalogservice.repository.AlbumRepository;
import com.revplay.catalogservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlbumService {
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private SongRepository songRepository;
    
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public Optional<Album> getAlbumById(Long id) {
        return albumRepository.findById(id);
    }
    
    public Optional<Album> getPublicAlbumById(Long id) {
        return albumRepository.findByIdAndIsPublicTrue(id);
    }
    
    public List<Album> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistId(artistId);
    }
    
    public List<Album> getPublicAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtistIdAndIsPublicTrue(artistId);
    }
    
    public List<Album> getAllPublicAlbums() {
        return albumRepository.findByIsPublicTrue();
    }
    
    public List<Album> searchAlbums(String name) {
        return albumRepository.findByNameContainingAndIsPublicTrue(name);
    }
    
    public Album updateAlbum(Album album) {
        return albumRepository.save(album);
    }
    
    public void deleteAlbum(Long id) {
        // Check if album has songs
        List<Song> songs = songRepository.findByAlbumId(id);
        if (!songs.isEmpty()) {
            throw new RuntimeException("Cannot delete album with existing songs. Remove songs first.");
        }
        albumRepository.deleteById(id);
    }
    
    public void updateAlbumStats(Long albumId) {
        Optional<Album> albumOpt = albumRepository.findById(albumId);
        if (albumOpt.isPresent()) {
            Album album = albumOpt.get();
            List<Song> songs = songRepository.findByAlbumId(albumId);
            album.setTotalSongs(songs.size());
            album.setTotalDuration(songs.stream()
                .mapToInt(song -> song.getDurationSeconds() != null ? song.getDurationSeconds() : 0)
                .sum());
            albumRepository.save(album);
        }
    }
}
