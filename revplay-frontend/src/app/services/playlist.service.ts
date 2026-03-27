import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Playlist, PlaylistSong, CreatePlaylistRequest, AddSongToPlaylistRequest } from '../models/playlist.model';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {
  private readonly API_URL = 'http://localhost:9080';
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  private handleError(error: any, context?: string): Observable<never> {
    console.error(`PlaylistService error in ${context}:`, error);
    return throwError(() => error);
  }

  // ✅ Create playlist - Connect to Playlist Service
  createPlaylist(request: CreatePlaylistRequest): Observable<Playlist> {
    return this.http.post<Playlist>(`${this.API_URL}/api/playlists`, request, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'createPlaylist')));
  }

  getPlaylistById(id: number): Observable<Playlist> {
    return this.http.get<Playlist>(`${this.API_URL}/api/playlists/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getPlaylistById')));
  }

  // ✅ Show user playlists
  getUserPlaylists(userId: number): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.API_URL}/api/playlists/user/${userId}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getUserPlaylists')));
  }

  getUserPublicPlaylists(userId: number): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.API_URL}/api/playlists/user/${userId}/public`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getUserPublicPlaylists')));
  }

  getPublicPlaylists(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.API_URL}/api/playlists/public`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getPublicPlaylists')));
  }

  searchPlaylists(query: string): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(`${this.API_URL}/api/playlists/search?q=${encodeURIComponent(query)}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'searchPlaylists')));
  }

  updatePlaylist(id: number, playlist: Partial<Playlist>): Observable<Playlist> {
    return this.http.put<Playlist>(`${this.API_URL}/api/playlists/${id}`, playlist, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'updatePlaylist')));
  }

  deletePlaylist(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/api/playlists/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'deletePlaylist')));
  }

  // ✅ Add songs to playlist
  addSongToPlaylist(playlistId: number, request: AddSongToPlaylistRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/api/playlists/${playlistId}/songs`, request, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'addSongToPlaylist')));
  }

  // ✅ Remove songs from playlist
  removeSongFromPlaylist(playlistId: number, songId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/api/playlists/${playlistId}/songs/${songId}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'removeSongFromPlaylist')));
  }

  getPlaylistSongs(playlistId: number): Observable<PlaylistSong[]> {
    return this.http.get<PlaylistSong[]>(`${this.API_URL}/api/playlists/${playlistId}/songs`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getPlaylistSongs')));
  }

  isSongInPlaylist(playlistId: number, songId: number): Observable<{ exists: boolean }> {
    return this.http.get<{ exists: boolean }>(`${this.API_URL}/api/playlists/${playlistId}/songs/${songId}/exists`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'isSongInPlaylist')));
  }

  getPlaylistSongCount(playlistId: number): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.API_URL}/api/playlists/${playlistId}/count`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getPlaylistSongCount')));
  }
}
