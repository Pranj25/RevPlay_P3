import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Song } from './song.service';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private currentSongSubject = new Subject<Song | null>();
  private isPlayingSubject = new Subject<boolean>();
  
  currentSong$ = this.currentSongSubject.asObservable();
  isPlaying$ = this.isPlayingSubject.asObservable();
  
  private currentSong: Song | null = null;
  private isPlaying = false;

  playSong(song: Song, playlist?: Song[]): void {
    this.currentSong = song;
    this.isPlaying = true;
    this.currentSongSubject.next(song);
    this.isPlayingSubject.next(true);
  }

  pauseSong(): void {
    this.isPlaying = false;
    this.isPlayingSubject.next(false);
  }

  resumeSong(): void {
    if (this.currentSong) {
      this.isPlaying = true;
      this.isPlayingSubject.next(true);
    }
  }

  stopSong(): void {
    this.currentSong = null;
    this.isPlaying = false;
    this.currentSongSubject.next(null);
    this.isPlayingSubject.next(false);
  }

  nextSong(playlist: Song[]): void {
    if (playlist.length > 0) {
      const currentIndex = this.currentSong ? playlist.findIndex(s => s.id === this.currentSong!.id) : -1;
      const nextIndex = currentIndex < playlist.length - 1 ? currentIndex + 1 : 0;
      this.playSong(playlist[nextIndex], playlist);
    }
  }

  previousSong(playlist: Song[]): void {
    if (playlist.length > 0) {
      const currentIndex = this.currentSong ? playlist.findIndex(s => s.id === this.currentSong!.id) : -1;
      const prevIndex = currentIndex > 0 ? currentIndex - 1 : playlist.length - 1;
      this.playSong(playlist[prevIndex], playlist);
    }
  }

  getCurrentSong(): Song | null {
    return this.currentSong;
  }

  getIsPlaying(): boolean {
    return this.isPlaying;
  }
}
