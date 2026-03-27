package com.revplay.catalogservice.service;

import com.revplay.catalogservice.entity.Song;
import com.revplay.catalogservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    
    @Autowired
    private SongRepository songRepository;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    public Song uploadSong(Song song, MultipartFile musicFile, MultipartFile coverImage) throws IOException {
        // Store music file
        String musicFilePath = fileStorageService.storeMusicFile(musicFile);
        song.setFilePath(musicFilePath);
        song.setFileSize(musicFile.getSize());
        song.setFileFormat(getFileExtension(musicFile.getOriginalFilename()));
        
        // Store cover image if provided
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverImagePath = fileStorageService.storeCoverImage(coverImage);
            song.setCoverImagePath(coverImagePath);
        }
        
        // Save song metadata
        return songRepository.save(song);
    }
    
    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }
    
    public List<Song> getAllSongs() {
        return songRepository.findByIsPublic(true);
    }
    
    public List<Song> getSongsByUser(Long userId) {
        return songRepository.findByUploadedBy(userId);
    }
    
    public List<Song> searchSongs(String query) {
        return songRepository.searchSongs(query);
    }
    
    public List<Song> getSongsByGenre(String genre) {
        return songRepository.findByGenre(genre);
    }
    
    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtistContainingIgnoreCase(artist);
    }
    
    public List<Song> getMostPlayedSongs() {
        return songRepository.findMostPlayedSongs();
    }
    
    public List<Song> getLatestSongs() {
        return songRepository.findLatestSongs();
    }
    
    public Song updateSong(Long id, Song songDetails) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        
        song.setTitle(songDetails.getTitle());
        song.setArtist(songDetails.getArtist());
        song.setAlbum(songDetails.getAlbum());
        song.setGenre(songDetails.getGenre());
        song.setLyrics(songDetails.getLyrics());
        song.setDurationSeconds(songDetails.getDurationSeconds());
        song.setIsPublic(songDetails.getIsPublic());
        
        return songRepository.save(song);
    }
    
    public void deleteSong(Long id) throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        
        // Delete files
        if (song.getFilePath() != null) {
            fileStorageService.deleteFile(song.getFilePath());
        }
        if (song.getCoverImagePath() != null) {
            fileStorageService.deleteFile(song.getCoverImagePath());
        }
        
        // Delete from database
        songRepository.deleteById(id);
    }
    
    public Song incrementPlayCount(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        
        song.setPlayCount(song.getPlayCount() + 1);
        return songRepository.save(song);
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
