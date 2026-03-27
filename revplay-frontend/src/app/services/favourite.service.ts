import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Song } from './song.service';

@Injectable({
  providedIn: 'root'
})
export class FavouriteService {
  private readonly API_URL = 'http://localhost:9080';
  private favoriteCache = new Set<number>();
  private favoritesSubject = new BehaviorSubject<Song[]>([]);
  public favorites$ = this.favoritesSubject.asObservable();
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  likeSong(songId: number, songTitle: string, songArtist: string): Observable<any> {
    return this.http.post(`${this.API_URL}/api/favourites/like`, {
      userId: this.getCurrentUserId(),
      songId,
      songTitle,
      songArtist
    }, { headers: this.getAuthHeaders() });
  }

  unlikeSong(userId: number, songId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/api/favourites/unlike?userId=${userId}&songId=${songId}`, { headers: this.getAuthHeaders() });
  }

  toggleLike(songId: number, songTitle: string, songArtist: string): Observable<any> {
    return this.http.post(`${this.API_URL}/api/favourites/toggle`, {
      userId: this.getCurrentUserId(),
      songId,
      songTitle,
      songArtist
    }, { headers: this.getAuthHeaders() });
  }

  isSongLiked(userId: number, songId: number): Observable<{ liked: boolean }> {
    return this.http.get<{ liked: boolean }>(`${this.API_URL}/api/favourites/check?userId=${userId}&songId=${songId}`, { headers: this.getAuthHeaders() });
  }

  getUserFavourites(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/favourites/user/${userId}`, { headers: this.getAuthHeaders() });
  }

  getSongFavourites(songId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/favourites/song/${songId}`, { headers: this.getAuthHeaders() });
  }

  getSongLikeCount(songId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.API_URL}/api/favourites/song/${songId}/count`, { headers: this.getAuthHeaders() });
  }

  getUserFavouriteCount(userId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.API_URL}/api/favourites/user/${userId}/count`, { headers: this.getAuthHeaders() });
  }

  getMultipleSongLikeCounts(songIds: number[]): Observable<{ [key: number]: number }> {
    return this.http.post<{ [key: number]: number }>(`${this.API_URL}/api/favourites/batch/likes`, songIds, { headers: this.getAuthHeaders() });
  }

  getMostLikedSongs(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/favourites/most-liked`, { headers: this.getAuthHeaders() });
  }

  // Add toggleFavorite method for components
  toggleFavorite(songId: number, songTitle: string, songArtist: string): Observable<any> {
    const request = {
      userId: this.getCurrentUserId(),
      songId,
      songTitle,
      songArtist,
      timestamp: new Date().toISOString()
    };

    return this.http.post(`${this.API_URL}/api/favorites/toggle`, request, {
      headers: this.getAuthHeaders()
    });
  }

  // Add isFavorite method for components
  isFavorite(songId: number): boolean {
    return this.favoriteCache.has(songId);
  }

  // Add getFavorites method for components
  getFavorites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/favorites/user/${this.getCurrentUserId()}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Add removeFavorite method for components
  removeFavorite(songId: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/api/favorites/${songId}`, {
      headers: this.getAuthHeaders()
    });
  }

  private getCurrentUserId(): number {
    const user = JSON.parse(localStorage.getItem('currentUser') || '{}');
    return user.id || 0;
  }
}
