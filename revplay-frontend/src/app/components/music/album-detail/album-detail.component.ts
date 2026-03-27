import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PlayerService } from '../../../services/player.service';
import { FavouriteService } from '../../../services/favourite.service';
import { AuthService } from '../../../services/auth.service';
import { Song, SongService } from '../../../services/song.service';

interface Album {
  id: number;
  name: string;
  description: string;
  coverArt: string;
  releaseDate: string;
  artist: {
    id: number;
    artistName: string;
  };
}

@Component({
  selector: 'app-album-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './album-detail.component.html',
  styleUrls: ['./album-detail.component.scss']
})
export class AlbumDetailComponent implements OnInit {
  album: Album | null = null;
  songs: Song[] = [];
  loading = true;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private songService: SongService,
    private playerService: PlayerService,
    public favouriteService: FavouriteService,
    public authService: AuthService
  ) {}
  
  ngOnInit() {
    const albumName = this.route.snapshot.paramMap.get('name');
    if (albumName) {
      this.loadAlbumSongs(decodeURIComponent(albumName));
    }
    if (this.authService.currentUser()) {
      this.favouriteService.getFavorites().subscribe();
    }
  }
  
  loadAlbumSongs(albumName: string) {
    this.http.get<Song[]>('http://localhost:8081/api/songs').subscribe({
      next: (allSongs) => {
        this.songs = allSongs
          .filter(song => song.album === albumName)
          .sort((a, b) => a.title.localeCompare(b.title));
        
        if (this.songs.length > 0) {
          this.album = {
            id: 1,
            name: albumName,
            description: `Album ${albumName}`,
            coverArt: '',
            releaseDate: new Date().toISOString(),
            artist: {
              id: 1,
              artistName: this.songs[0].artist
            }
          };
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading songs:', err);
        this.loading = false;
      }
    });
  }
  
  playSong(song: Song) {
    this.playerService.playSong(song, this.songs);
  }
  
  toggleFavorite(event: Event, songId: number, songTitle: string, songArtist: string) {
    event.stopPropagation();
    if (!this.authService.currentUser()) {
      alert('Please login to add favorites');
      return;
    }
    this.favouriteService.toggleFavorite(songId, songTitle, songArtist).subscribe({
      error: (err: any) => console.error('Error toggling favorite:', err)
    });
  }
  
  getSongCover(song: Song): string {
    return song.coverImagePath 
      ? `http://localhost:8081/api/catalog/songs/cover/${song.id}`
      : 'http://localhost:8081/images/default-cover.svg';
  }
  
  getAlbumCover(): string {
    if (!this.album?.coverArt) return 'http://localhost:8081/images/default-cover.svg';
    return `http://localhost:8081${this.album.coverArt}`;
  }
  
  goBack() {
    this.router.navigate(['/albums']);
  }
}