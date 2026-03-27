import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { PlaybackService } from '../../../services/playback.service';
import { FavouriteService } from '../../../services/favourite.service';
import { AnalyticsService } from '../../../services/analytics.service';
import { Song } from '../../../models/song.model';

@Component({
  selector: 'app-player',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('audioPlayer') audioPlayer!: ElementRef<HTMLAudioElement>;
  
  currentSong: Song | null = null;
  isPlaying = false;
  currentTime = 0;
  duration = 0;
  volume = 0.8;
  isRepeat = false;
  isShuffle = false;
  isLoading = false;
  isFavorite = false;
  playbackHistory: Song[] = [];
  currentPlaybackId: number | null = null;
  
  private playbackSubscription: Subscription | null = null;
  private currentUser: any = null;

  constructor(
    public authService: AuthService,
    private playbackService: PlaybackService,
    private favouriteService: FavouriteService,
    private analyticsService: AnalyticsService
  ) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUserValue();
    
    // ✅ Listen for play song events from other components
    window.addEventListener('playSong', this.handlePlaySong.bind(this));
    window.addEventListener('pauseSong', this.handlePauseSong.bind(this));
    window.addEventListener('nextSong', this.handleNextSong.bind(this));
    window.addEventListener('previousSong', this.handlePreviousSong.bind(this));
    
    // Load volume from localStorage
    const savedVolume = localStorage.getItem('playerVolume');
    if (savedVolume) {
      this.volume = parseFloat(savedVolume);
    }
  }

  ngAfterViewInit() {
    if (this.audioPlayer) {
      this.setupAudioListeners();
    }
  }

  ngOnDestroy() {
    this.playbackSubscription?.unsubscribe();
    window.removeEventListener('playSong', this.handlePlaySong.bind(this));
    window.removeEventListener('pauseSong', this.handlePauseSong.bind(this));
    window.removeEventListener('nextSong', this.handleNextSong.bind(this));
    window.removeEventListener('previousSong', this.handlePreviousSong.bind(this));
  }

  private setupAudioListeners() {
    if (this.audioPlayer) {
      const audio = this.audioPlayer.nativeElement;
      
      audio.addEventListener('timeupdate', () => {
        this.currentTime = audio.currentTime;
      });

      audio.addEventListener('loadedmetadata', () => {
        this.duration = audio.duration;
      });

      audio.addEventListener('ended', () => {
        this.handleSongEnd();
      });

      audio.addEventListener('error', (error) => {
        console.error('Audio error:', error);
        this.isLoading = false;
      });
    }
  }

  // Play songs dynamically with streaming URL from gateway
  playSong(song: Song) {
    if (!song) return;

    this.currentSong = song;
    this.isLoading = true;

    // Get streaming URL from gateway
    const streamingUrl = this.getStreamingUrl(song);
    
    // Record play event to analytics
    this.recordPlayEvent(song);

    // Set audio source and play
    if (this.audioPlayer) {
      const audio = this.audioPlayer.nativeElement;
      audio.src = streamingUrl;
      audio.load();
      
      audio.play().then(() => {
        this.isPlaying = true;
        this.isLoading = false;
        this.recordPlaybackStart(song);
      }).catch(error => {
        console.error('Error playing song:', error);
        this.isLoading = false;
      });
    }
  }

  private getStreamingUrl(song: Song): string {
    // Use API Gateway streaming endpoint
    return `http://localhost:9080/api/playback/stream/${song.id}?filePath=${encodeURIComponent(song.filePath)}`;
  }

  private recordPlayEvent(song: Song) {
    if (!this.currentUser) return;

    // Send analytics event
    this.analyticsService.recordSongPlay(
      song.id || 0,
      song.title || '',
      song.artist || '',
      song.durationSeconds || 0,
      false // not completed yet
    ).subscribe({
      error: (err) => console.error('Error recording play event:', err)
    });
  }

  private recordPlaybackStart(song: Song) {
    if (!this.currentUser) return;

    // Record playback start event
    this.playbackService.recordPlayback(
      this.currentUser.id || 0,
      song.id || 0,
      song.title || '',
      song.artist || '',
      song.durationSeconds || 0,
      0, // playedDuration starts at 0
      false // not completed yet
    ).subscribe({
      error: (err) => console.error('Error recording playback:', err)
    });
  }

  private recordPlaybackComplete(song: Song, playedDuration: number) {
    if (!this.currentUser) return;

    // Record playback completion
    this.playbackService.recordPlayback(
      this.currentUser.id || 0,
      song.id || 0,
      song.title || '',
      song.artist || '',
      song.durationSeconds || 0,
      playedDuration,
      true // completed
    ).subscribe({
      error: (err) => console.error('Error recording playback completion:', err)
    });

    // Update analytics
    this.analyticsService.recordSongPlay(
      song.id || 0,
      song.title || '',
      song.artist || '',
      song.durationSeconds || 0,
      true // completed
    ).subscribe({
      error: (err) => console.error('Error recording completion:', err)
    });
  }

  // Player controls
  togglePlay() {
    if (!this.audioPlayer || !this.currentSong) return;

    const audio = this.audioPlayer.nativeElement;

    if (this.isPlaying) {
      audio.pause();
      this.isPlaying = false;
    } else {
      audio.play().then(() => {
        this.isPlaying = true;
      }).catch(error => {
        console.error('Error playing:', error);
      });
    }
  }

  stop() {
    if (!this.audioPlayer) return;

    const audio = this.audioPlayer.nativeElement;
    audio.pause();
    audio.currentTime = 0;
    this.isPlaying = false;
    this.currentSong = null;
  }

  next() {
    // Implement next song logic (would need playlist context)
    console.log('Next song - requires playlist context');
  }

  previous() {
    // Implement previous song logic (would need playlist context)
    console.log('Previous song - requires playlist context');
  }

  toggleRepeat() {
    this.isRepeat = !this.isRepeat;
  }

  toggleShuffle() {
    this.isShuffle = !this.isShuffle;
  }

  // Seek and volume controls
  onSeek(event: any) {
    if (!this.audioPlayer) return;

    const audio = this.audioPlayer.nativeElement;
    const seekTime = (event.target.value / 100) * audio.duration;
    audio.currentTime = seekTime;
  }

  onVolumeChange(event: any) {
    if (!this.audioPlayer) return;

    const audio = this.audioPlayer.nativeElement;
    this.volume = event.target.value;
    audio.volume = this.volume;
  }

  // Favorite functionality
  toggleFavorite() {
    if (!this.currentUser) {
      alert('Please login to add favorites');
      return;
    }

    if (!this.currentSong) return;

    this.favouriteService.toggleLike(
      this.currentSong.id || 0,
      this.currentSong.title || '',
      this.currentSong.artist || ''
    ).subscribe({
      next: (response) => {
        console.log('Favorite toggled:', response);
      },
      error: (err) => {
        console.error('Error toggling favorite:', err);
      }
    });
  }

  // ✅ Event handler methods for external control
  private handlePlaySong(event: CustomEvent) {
    this.playSong(event.detail);
  }

  private handlePauseSong() {
    if (this.audioPlayer) {
      const audio = this.audioPlayer.nativeElement;
      audio.pause();
      this.isPlaying = false;
    }
  }

  private handleNextSong() {
    this.next();
  }

  private handlePreviousSong() {
    this.previous();
  }

  // Track current song methods
  getCurrentSong(): Song | null {
    return this.currentSong;
  }

  getCurrentSongTitle(): string {
    return this.currentSong?.title || 'No song playing';
  }

  getCurrentSongArtist(): string {
    return this.currentSong?.artist || '';
  }

  // Utility methods
  private handleSongEnd() {
    if (!this.currentSong) return;

    const playedDuration = this.currentTime;
    this.recordPlaybackComplete(this.currentSong, playedDuration);

    if (this.isRepeat) {
      // Repeat current song
      this.playSong(this.currentSong);
    } else {
      // Move to next song (would need playlist context)
      this.next();
    }
  }

  formatTime(seconds: number): string {
    if (!seconds || isNaN(seconds)) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  }

  getProgressPercentage(): number {
    if (!this.duration) return 0;
    return (this.currentTime / this.duration) * 100;
  }

  getSongCover(): string {
    if (!this.currentSong) return 'assets/images/default-cover.svg';
    
    if (this.currentSong.coverImagePath) {
      return `http://localhost:9080/api/catalog/songs/cover/${this.currentSong.id}`;
    }
    
    return 'assets/images/default-cover.svg';
  }

  // Public method to set current song from other components
  setCurrentSong(song: Song) {
    this.currentSong = song;
    this.playSong(song);
  }
}
