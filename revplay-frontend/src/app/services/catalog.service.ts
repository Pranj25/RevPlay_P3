import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Song, SongSearchRequest } from '../models/song.model';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
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
    console.error(`CatalogService error in ${context}:`, error);
    return throwError(() => error);
  }

  // ✅ Fetch songs - Connect to Catalog Service
  getAllSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getAllSongs')));
  }

  getSongById(id: number): Observable<Song> {
    return this.http.get<Song>(`${this.API_URL}/api/catalog/songs/${id}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getSongById')));
  }

  // ✅ Search songs - Connect to Catalog Service
  searchSongs(searchRequest: SongSearchRequest): Observable<Song[]> {
    const params = new HttpParams()
      .set('query', searchRequest.query || '')
      .set('genre', searchRequest.genre || '')
      .set('artist', searchRequest.artist || '')
      .set('limit', searchRequest.limit?.toString() || '20')
      .set('offset', searchRequest.offset?.toString() || '0');

    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/search`, { 
      headers: this.getAuthHeaders(),
      params 
    }).pipe(catchError(error => this.handleError(error, 'searchSongs')));
  }

  // ✅ Display songs - Get songs by genre
  getSongsByGenre(genre: string): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/genre/${genre}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getSongsByGenre')));
  }

  // ✅ Display songs - Get songs by artist
  getSongsByArtist(artist: string): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/artist/${artist}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getSongsByArtist')));
  }

  // ✅ Get most played songs
  getMostPlayedSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/most-played`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getMostPlayedSongs')));
  }

  // ✅ Get latest songs
  getLatestSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/latest`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getLatestSongs')));
  }

  // ✅ Get song cover URL
  getSongCover(songId: number): string {
    return `${this.API_URL}/api/catalog/songs/cover/${songId}`;
  }

  // ✅ Get albums
  getAlbums(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/catalog/albums`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getAlbums')));
  }

  // ✅ Get artists
  getArtists(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/catalog/artists`, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'getArtists')));
  }

  uploadSong(formData: FormData): Observable<any> {
    return this.http.post(`${this.API_URL}/api/catalog/songs/upload`, formData, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'uploadSong')));
  }

  incrementPlayCount(id: number): Observable<Song> {
    return this.http.post<Song>(`${this.API_URL}/api/catalog/songs/${id}/play`, {}, { headers: this.getAuthHeaders() })
      .pipe(catchError(error => this.handleError(error, 'incrementPlayCount')));
  }

  downloadSong(id: number): Observable<Blob> {
    return this.http.get(`${this.API_URL}/api/catalog/songs/download/${id}`, { 
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    }).pipe(catchError(error => this.handleError(error, 'downloadSong')));
  }

  getCoverImage(id: number): Observable<Blob> {
    return this.http.get(`${this.API_URL}/api/catalog/songs/cover/${id}`, { 
      headers: this.getAuthHeaders(),
      responseType: 'blob'
    }).pipe(catchError(error => this.handleError(error, 'getCoverImage')));
  }
}
