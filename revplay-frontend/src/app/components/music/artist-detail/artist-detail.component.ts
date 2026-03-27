import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PlayerService } from '../../../services/player.service';
import { FavouriteService } from '../../../services/favourite.service';
import { AuthService } from '../../../services/auth.service';
import { SongService } from '../../../services/song.service';

interface Artist {
  id: number;
  artistName: string;
  bio: string;
  genre: string;
  profilePicture: string;
}

interface Song {
  id: number;
  title: string;
  artist: string;
  album?: string;
  genre?: string;
  durationSeconds?: number;
  filePath?: string;
  coverImagePath?: string;
  coverImage?: string;
  createdAt?: string;
  updatedAt?: string;
}

@Component({
  selector: 'app-artist-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './artist-detail.component.html',
  styleUrls: ['./artist-detail.component.scss']
})
export class ArtistDetailComponent implements OnInit {
  artist: Artist | null = null;
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
    const artistName = this.route.snapshot.paramMap.get('name');
    if (artistName) {
      this.loadArtistSongs(decodeURIComponent(artistName));
    }
    if (this.authService.currentUser()) {
      this.favouriteService.getFavorites().subscribe();
    }
  }
  
  loadArtistSongs(artistName: string) {
    this.http.get<Song[]>('http://localhost:8081/api/songs').subscribe({
      next: (allSongs) => {
        this.songs = allSongs
          .filter(song => song.artist.toLowerCase() === artistName.toLowerCase())
          .sort((a, b) => a.title.localeCompare(b.title));
        
        if (this.songs.length > 0) {
          this.artist = {
            id: 1,
            artistName: this.songs[0].artist,
            bio: '',
            genre: '',
            profilePicture: ''
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
    return song.coverImage 
      ? `http://localhost:8081${song.coverImage}`
      : 'http://localhost:8081/images/default-cover.svg';
  }
  
  goBack() {
    this.router.navigate(['/artists']);
  }
}
