import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { PlaybackService } from '../../../services/playback.service';
import { FavouriteService } from '../../../services/favourite.service';
import { AuthService } from '../../../services/auth.service';
import { Song } from '../../../models/song.model';

@Component({
  selector: 'app-songs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.scss']
})
export class SongsComponent implements OnInit {
  songs: Song[] = [];
  loading = true;
  error = '';
  
  constructor(
    private catalogService: CatalogService,
    private playbackService: PlaybackService,
    private favouriteService: FavouriteService,
    public authService: AuthService,
    public favoriteService: FavouriteService,
    private router: Router
  ) {}
  
  ngOnInit() {
    // Check authentication state
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    
    this.loadSongs();
  }
  
  loadSongs() {
    this.catalogService.getAllSongs().subscribe({
      next: (songs: Song[]) => {
        this.songs = songs;
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Failed to load songs';
        this.loading = false;
        console.error('Error loading songs:', err);
      }
    });
  }
  
  playSong(song: Song) {
    // Record play event
    const currentUser = this.authService.getCurrentUserValue();
    if (currentUser) {
      this.playbackService.recordPlayback(
        currentUser.id || 0,
        song.id || 0,
        song.title || '',
        song.artist || '',
        song.durationSeconds || 0,
        0, // playedDuration starts at 0
        false // not completed yet
      ).subscribe({
        error: (err: any) => console.error('Error recording playback:', err)
      });
    }

    // Increment play count
    this.catalogService.incrementPlayCount(song.id || 0).subscribe({
      error: (err: any) => console.error('Error incrementing play count:', err)
    });

    // Emit play event for player component
    window.dispatchEvent(new CustomEvent('playSong', { 
      detail: song 
    }));
  }
  
  toggleFavorite(event: Event, song: Song) {
    event.stopPropagation();
    
    if (!this.authService.isLoggedIn()) {
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
      error: (err: any) => {
        console.error('Error toggling favorite:', err);
      }
    });
  }
  
  getSongCover(song: Song): string {
    // Use API Gateway streaming endpoint for cover images
    if (song.coverImagePath) {
      return `http://localhost:9080/api/catalog/songs/cover/${song.id}`;
    }
    return 'assets/images/default-cover.svg';
  }
  
  formatDuration(seconds: number): string {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  }
}
