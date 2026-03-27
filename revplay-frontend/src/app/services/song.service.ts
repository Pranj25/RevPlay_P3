import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Song {
  id: number;
  title: string;
  artist: string;
  album?: string;
  genre?: string;
  durationSeconds?: number;
  filePath?: string;
  coverImagePath?: string;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class SongService {
  private readonly API_URL = 'http://localhost:9080';

  constructor(private http: HttpClient) {}

  getAllSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs`);
  }

  getSongById(id: number): Observable<Song> {
    return this.http.get<Song>(`${this.API_URL}/api/catalog/songs/${id}`);
  }

  searchSongs(query: string): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/search?q=${query}`);
  }

  getSongsByGenre(genre: string): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/genre/${genre}`);
  }

  getLatestSongs(limit: number = 10): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/latest?limit=${limit}`);
  }

  getMostPlayedSongs(limit: number = 10): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.API_URL}/api/catalog/songs/most-played?limit=${limit}`);
  }
}
