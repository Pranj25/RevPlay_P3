import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { PlaybackService } from '../../../services/playback.service';
import { FavouriteService } from '../../../services/favourite.service';
import { AuthService } from '../../../services/auth.service';
import { Song } from '../../../models/song.model';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {
  searchQuery = '';
  songs: Song[] = [];
  allSongs: Song[] = [];
  loading = false;
  searched = false;
  
  constructor(
    private catalogService: CatalogService,
    private playbackService: PlaybackService,
    private favouriteService: FavouriteService,
    public authService: AuthService,
    public favoriteService: FavouriteService,
    private router: Router
  ) {
    this.loadAllSongs();
  }
  
  loadAllSongs() {
    this.catalogService.getAllSongs().subscribe({
      next: (songs: Song[]) => {
        this.allSongs = songs;
      },
      error: (err: any) => console.error('Error loading songs:', err)
    });
  }
  
  search() {
    if (!this.searchQuery.trim()) {
      this.songs = [];
      this.searched = false;
      return;
    }
    
    this.loading = true;
    this.searched = true;
    
    // Use catalog service search instead of client-side filtering
    this.catalogService.searchSongs({ 
      query: this.searchQuery, 
      limit: 50 
    }).subscribe({
      next: (songs: Song[]) => {
        this.songs = songs;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error searching songs:', err);
        this.loading = false;
        // Fallback to client-side search if service fails
        const query = this.searchQuery.toLowerCase();
        this.songs = this.allSongs.filter(song => 
          song.title.toLowerCase().includes(query) ||
          song.artist.toLowerCase().includes(query) ||
          (song.album && song.album.toLowerCase().includes(query)) ||
          (song.genre && song.genre.toLowerCase().includes(query))
        );
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
}
