import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private readonly API_URL = 'http://localhost:9080';
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  recordSongPlay(songId: number, songTitle: string, songArtist: string, duration: number, completed: boolean): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/song/play`, null, {
      params: {
        songId: songId.toString(),
        songTitle,
        songArtist,
        duration: duration.toString(),
        completed: completed.toString()
      },
      headers: this.getAuthHeaders()
    });
  }

  recordSongLike(songId: number, songTitle: string, songArtist: string): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/song/like`, null, {
      params: { songId: songId.toString(), songTitle, songArtist },
      headers: this.getAuthHeaders()
    });
  }

  recordSongShare(songId: number, songTitle: string, songArtist: string): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/song/share`, null, {
      params: { songId: songId.toString(), songTitle, songArtist },
      headers: this.getAuthHeaders()
    });
  }

  recordUserActivity(userId: number, playTime: number, songsPlayed: number, uniqueSongs: number, newSession: boolean): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/user/activity`, null, {
      params: {
        userId: userId.toString(),
        playTime: playTime.toString(),
        songsPlayed: songsPlayed.toString(),
        uniqueSongs: uniqueSongs.toString(),
        newSession: newSession.toString()
      },
      headers: this.getAuthHeaders()
    });
  }

  recordUserLike(userId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/user/like`, null, {
      params: { userId: userId.toString() },
      headers: this.getAuthHeaders()
    });
  }

  recordPlaylistCreation(userId: number): Observable<any> {
    return this.http.post(`${this.API_URL}/api/analytics/user/playlist`, null, {
      params: { userId: userId.toString() },
      headers: this.getAuthHeaders()
    });
  }

  getSongReport(songId: number, startDate: string, endDate: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/reports/song/${songId}?startDate=${startDate}&endDate=${endDate}`, { headers: this.getAuthHeaders() });
  }

  getUserReport(userId: number, startDate: string, endDate: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/reports/user/${userId}?startDate=${startDate}&endDate=${endDate}`, { headers: this.getAuthHeaders() });
  }

  getPlatformReport(startDate: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/reports/platform?startDate=${startDate}`, { headers: this.getAuthHeaders() });
  }

  getTopPlayedSongs(days: number = 30, limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/trends/top-songs?days=${days}&limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getTopArtists(days: number = 30, limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/trends/top-artists?days=${days}&limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getTopGenres(days: number = 30, limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/trends/top-genres?days=${days}&limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getTopUsersByPlayTime(days: number = 30, limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/analytics/trends/top-users?days=${days}&limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getSongSummary(songId: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/api/analytics/summary/song/${songId}`, { headers: this.getAuthHeaders() });
  }

  getUserSummary(userId: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/api/analytics/summary/user/${userId}`, { headers: this.getAuthHeaders() });
  }

  getPlatformSummary(days: number = 30): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/api/analytics/summary/platform?days=${days}`, { headers: this.getAuthHeaders() });
  }
}
