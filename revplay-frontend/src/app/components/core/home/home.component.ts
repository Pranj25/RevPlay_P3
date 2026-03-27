import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { CatalogService } from '../../../services/catalog.service';
import { PlaylistService } from '../../../services/playlist.service';
import { FavouriteService } from '../../../services/favourite.service';
import { PlaybackService } from '../../../services/playback.service';
import { Song } from '../../../models/song.model';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  songs: Song[] = [];
  latestSongs: Song[] = [];
  mostPlayedSongs: Song[] = [];
  loading = true;
  currentUser: User | null = null;
  
  constructor(
    public authService: AuthService,
    private catalogService: CatalogService,
    private playlistService: PlaylistService,
    private favouriteService: FavouriteService,
    private playbackService: PlaybackService
  ) {}
  
  ngOnInit() {
    this.currentUser = this.authService.getCurrentUserValue();
    this.loadSongs();
    this.loadLatestSongs();
    this.loadMostPlayedSongs();
  }
  
  // 1. Catalog Service - Fetch songs list
  loadSongs() {
    this.catalogService.getAllSongs().subscribe({
      next: (songs) => {
        this.songs = songs.slice(0, 6); // Show first 6 songs
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading songs:', err);
        this.loading = false;
      }
    });
  }

  loadLatestSongs() {
    this.catalogService.getLatestSongs().subscribe({
      next: (songs) => {
        this.latestSongs = songs.slice(0, 6);
      },
      error: (err) => {
        console.error('Error loading latest songs:', err);
      }
    });
  }

  loadMostPlayedSongs() {
    this.catalogService.getMostPlayedSongs().subscribe({
      next: (songs) => {
        this.mostPlayedSongs = songs.slice(0, 6);
      },
      error: (err) => {
        console.error('Error loading most played songs:', err);
      }
    });
  }

  // 2. Catalog Service - Search songs
  searchSongs(query: string) {
    if (!query.trim()) {
      this.loadSongs();
      return;
    }

    this.catalogService.searchSongs({ query: query, limit: 10 }).subscribe({
      next: (songs) => {
        this.songs = songs;
      },
      error: (err) => {
        console.error('Error searching songs:', err);
      }
    });
  }

  // 3. Playback Service - Stream songs in audio player
  playSong(song: Song) {
    // Record play event
    this.playbackService.recordPlayback(
      this.currentUser?.id || 0,
      song.id || 0,
      song.title || '',
      song.artist || '',
      song.durationSeconds || 0,
      0, // playedDuration starts at 0
      false // not completed yet
    ).subscribe({
      error: (err) => console.error('Error recording playback:', err)
    });

    // Increment play count
    this.catalogService.incrementPlayCount(song.id || 0).subscribe({
      error: (err) => console.error('Error incrementing play count:', err)
    });

    // Find and use the player component to play the song
    // This would typically be done through a service or event system
    console.log('Playing song:', song.title);
    
    // For now, we'll emit a custom event that the player can listen to
    window.dispatchEvent(new CustomEvent('playSong', { 
      detail: song 
    }));
  }

  // 4. Favourite Service - Like/unlike songs
  toggleFavorite(event: Event, song: Song) {
    event.stopPropagation();
    
    if (!this.currentUser) {
      alert('Please login to add favorites');
      return;
    }

    this.favouriteService.toggleLike(
      song.id || 0,
      song.title || '',
      song.artist || ''
    ).subscribe({
      next: (response) => {
        console.log('Favorite toggled:', response);
        // Refresh songs to update like status
        this.loadSongs();
      },
      error: (err) => {
        console.error('Error toggling favorite:', err);
      }
    });
  }

  isSongLiked(songId: number): boolean {
    // This would come from favorite service or local state
    // For now, return false as placeholder
    return false;
  }

  // 5. Playlist Service - Create playlist
  createPlaylistWithSong(song: Song) {
    if (!this.currentUser) {
      alert('Please login to create playlists');
      return;
    }

    const playlistName = prompt('Enter playlist name:');
    if (!playlistName) return;

    this.playlistService.createPlaylist({
      name: playlistName,
      description: `Playlist created with ${song.title}`,
      isPublic: false
    }).subscribe({
      next: (playlist) => {
        console.log('Playlist created:', playlist);
        // Add song to playlist
        this.addSongToPlaylist(playlist.id || 0, song);
      },
      error: (err) => {
        console.error('Error creating playlist:', err);
      }
    });
  }

  addSongToPlaylist(playlistId: number, song: Song) {
    this.playlistService.addSongToPlaylist(playlistId, {
      songId: song.id || 0,
      songTitle: song.title || '',
      songArtist: song.artist || ''
    }).subscribe({
      next: (response) => {
        console.log('Song added to playlist:', response);
        alert('Song added to playlist successfully!');
      },
      error: (err) => {
        console.error('Error adding song to playlist:', err);
      }
    });
  }

  // Helper method to get song cover URL
  getSongCover(song: Song): string {
    if (song.coverImagePath) {
      return `http://localhost:9080/api/catalog/songs/cover/${song.id}`;
    }
    return 'assets/images/default-cover.svg';
  }

  // Helper method to format duration
  formatDuration(seconds: number): string {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = Math.floor(seconds % 60);
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  }
}
