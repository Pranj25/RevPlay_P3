package com.revplay.playlistservice.service;

import com.revplay.playlistservice.entity.Playlist;
import com.revplay.playlistservice.entity.PlaylistSong;
import com.revplay.playlistservice.entity.PlaylistFollow;
import com.revplay.playlistservice.repository.PlaylistRepository;
import com.revplay.playlistservice.repository.PlaylistSongRepository;
import com.revplay.playlistservice.repository.PlaylistFollowRepository;
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
    
    @Autowired
    private PlaylistFollowRepository playlistFollowRepository;
    
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
    
    // Follow/Unfollow functionality
    public PlaylistFollow followPlaylist(Long userId, Long playlistId) {
        // Check if playlist exists and is public
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        
        if (!playlist.getIsPublic()) {
            throw new RuntimeException("Cannot follow private playlist");
        }
        
        // Check if already following
        Optional<PlaylistFollow> existingFollow = playlistFollowRepository.findByUserIdAndPlaylistId(userId, playlistId);
        if (existingFollow.isPresent()) {
            throw new RuntimeException("Already following this playlist");
        }
        
        PlaylistFollow follow = new PlaylistFollow(userId, playlistId);
        PlaylistFollow savedFollow = playlistFollowRepository.save(follow);
        
        // Update follower count
        updatePlaylistFollowerCount(playlistId);
        
        return savedFollow;
    }
    
    public void unfollowPlaylist(Long userId, Long playlistId) {
        PlaylistFollow follow = playlistFollowRepository.findByUserIdAndPlaylistId(userId, playlistId)
                .orElseThrow(() -> new RuntimeException("Not following this playlist"));
        
        playlistFollowRepository.delete(follow);
        
        // Update follower count
        updatePlaylistFollowerCount(playlistId);
    }
    
    public boolean isFollowingPlaylist(Long userId, Long playlistId) {
        return playlistFollowRepository.findByUserIdAndPlaylistId(userId, playlistId).isPresent();
    }
    
    public List<PlaylistFollow> getUserFollowedPlaylists(Long userId) {
        return playlistFollowRepository.findByUserId(userId);
    }
    
    public List<PlaylistFollow> getPlaylistFollowers(Long playlistId) {
        return playlistFollowRepository.findByPlaylistId(playlistId);
    }
    
    public Long getPlaylistFollowerCount(Long playlistId) {
        return playlistFollowRepository.countByPlaylistId(playlistId);
    }
    
    public Long getUserFollowingCount(Long userId) {
        return playlistFollowRepository.countByUserId(userId);
    }
    
    public List<Playlist> getPublicPlaylistsByUser(Long userId) {
        return playlistRepository.findByUserIdAndIsPublicTrue(userId);
    }
    
    public void reorderPlaylistSongs(Long playlistId, Integer fromPosition, Integer toPosition) {
        List<PlaylistSong> songs = playlistSongRepository.findByPlaylistIdOrderByPositionOrderAsc(playlistId);
        
        if (fromPosition >= 1 && fromPosition <= songs.size() && 
            toPosition >= 1 && toPosition <= songs.size()) {
            
            PlaylistSong songToMove = songs.get(fromPosition - 1);
            
            if (fromPosition < toPosition) {
                // Moving down
                for (int i = fromPosition - 1; i < toPosition - 1; i++) {
                    songs.get(i).setPositionOrder(i + 1);
                    playlistSongRepository.save(songs.get(i));
                }
            } else {
                // Moving up
                for (int i = toPosition - 1; i < fromPosition - 1; i++) {
                    songs.get(i).setPositionOrder(i + 2);
                    playlistSongRepository.save(songs.get(i));
                }
            }
            
            songToMove.setPositionOrder(toPosition);
            playlistSongRepository.save(songToMove);
        }
    }
    
    private void updatePlaylistFollowerCount(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
        if (playlist != null) {
            Long followerCount = playlistFollowRepository.countByPlaylistId(playlistId);
            playlist.setFollowerCount(followerCount.intValue());
            playlistRepository.save(playlist);
        }
    }
}
