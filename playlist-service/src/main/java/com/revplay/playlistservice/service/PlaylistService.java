package com.revplay.playlistservice.service;

import com.revplay.playlistservice.entity.Playlist;
import com.revplay.playlistservice.entity.PlaylistSong;
import com.revplay.playlistservice.repository.PlaylistRepository;
import com.revplay.playlistservice.repository.PlaylistSongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaylistService {
    
    @Autowired
    private PlaylistRepository playlistRepository;
    
    @Autowired
    private PlaylistSongRepository playlistSongRepository;
    
    public Playlist createPlaylist(Playlist playlist) {
        // Check if playlist name already exists for this user
        if (playlistRepository.existsByNameAndUserId(playlist.getName(), playlist.getUserId())) {
            throw new RuntimeException("Playlist with this name already exists");
        }
        
        return playlistRepository.save(playlist);
    }
    
    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }
    
    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByUserId(userId);
    }
    
    public List<Playlist> getUserPublicPlaylists(Long userId) {
        return playlistRepository.findByUserIdAndIsPublic(userId, true);
    }
    
    public List<Playlist> getPublicPlaylists() {
        return playlistRepository.findByIsPublic(true);
    }
    
    public List<Playlist> searchPlaylists(String query) {
        return playlistRepository.searchPlaylists(query);
    }
    
    public Playlist updatePlaylist(Long id, Playlist playlistDetails) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        playlist.setName(playlistDetails.getName());
        playlist.setDescription(playlistDetails.getDescription());
        playlist.setIsPublic(playlistDetails.getIsPublic());
        playlist.setCoverImagePath(playlistDetails.getCoverImagePath());
        
        return playlistRepository.save(playlist);
    }
    
    public void deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        // Delete all playlist songs first
        playlistSongRepository.deleteByPlaylistIdAndSongId(id, null);
        
        // Delete playlist
        playlistRepository.deleteById(id);
    }
    
    public PlaylistSong addSongToPlaylist(Long playlistId, Long songId, String songTitle, String songArtist) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        // Check if song already exists in playlist
        if (playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId).isPresent()) {
            throw new RuntimeException("Song already exists in playlist");
        }
        
        // Get next position order
        Integer maxPosition = playlistSongRepository.findMaxPositionOrder(playlistId);
        Integer nextPosition = (maxPosition != null) ? maxPosition + 1 : 1;
        
        PlaylistSong playlistSong = new PlaylistSong(playlist, songId, songTitle, songArtist);
        playlistSong.setPositionOrder(nextPosition);
        
        return playlistSongRepository.save(playlistSong);
    }
    
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId)
                .orElseThrow(() -> new RuntimeException("Song not found in playlist"));
        
        playlistSongRepository.delete(playlistSong);
        
        // Reorder remaining songs
        reorderPlaylistSongs(playlistId);
    }
    
    public List<PlaylistSong> getPlaylistSongs(Long playlistId) {
        return playlistSongRepository.findByPlaylistIdOrderByPositionOrderAsc(playlistId);
    }
    
    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        return playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId).isPresent();
    }
    
    public Long getPlaylistSongCount(Long playlistId) {
        return playlistSongRepository.countSongsInPlaylist(playlistId);
    }
    
    private void reorderPlaylistSongs(Long playlistId) {
        List<PlaylistSong> songs = playlistSongRepository.findByPlaylistIdOrderByPositionOrderAsc(playlistId);
        
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setPositionOrder(i + 1);
            playlistSongRepository.save(songs.get(i));
        }
    }
}
