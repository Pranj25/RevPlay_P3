import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PlaybackService {
  private readonly API_URL = 'http://localhost:9080';
  
  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  recordPlayback(userId: number, songId: number, songTitle: string, songArtist: string, 
                 duration: number, playedDuration: number, completed: boolean): Observable<any> {
    return this.http.post(`${this.API_URL}/api/playback/record`, {
      userId,
      songId,
      songTitle,
      songArtist,
      durationSeconds: duration,
      playedDurationSeconds: playedDuration,
      completed
    }, { headers: this.getAuthHeaders() });
  }

  streamSong(songId: number, filePath: string, range?: string): Observable<Blob> {
    const headers = this.getAuthHeaders();
    if (range) {
      headers.set('Range', range);
    }
    
    return this.http.get(`${this.API_URL}/api/playback/stream/${songId}?filePath=${encodeURIComponent(filePath)}`, {
      headers,
      responseType: 'blob'
    });
  }

  getStreamInfo(songId: number, filePath: string): Observable<any> {
    return this.http.get(`${this.API_URL}/api/playback/stream/info/${songId}?filePath=${encodeURIComponent(filePath)}`, { headers: this.getAuthHeaders() });
  }

  getUserPlayHistory(userId: number, limit: number = 50): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/playback/history/user/${userId}?limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getUserPlayHistoryInRange(userId: number, startDate: string, endDate: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/playback/history/user/${userId}/range?startDate=${startDate}&endDate=${endDate}`, { headers: this.getAuthHeaders() });
  }

  getSongPlayHistory(songId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/playback/history/song/${songId}`, { headers: this.getAuthHeaders() });
  }

  getUserTotalStats(userId: number): Observable<{ totalPlays: number }> {
    return this.http.get<{ totalPlays: number }>(`${this.API_URL}/api/playback/stats/user/${userId}/total`, { headers: this.getAuthHeaders() });
  }

  getSongTotalStats(songId: number): Observable<{ totalPlays: number }> {
    return this.http.get<{ totalPlays: number }>(`${this.API_URL}/api/playback/stats/song/${songId}/total`, { headers: this.getAuthHeaders() });
  }

  getUserSongStats(userId: number, songId: number): Observable<{ playCount: number }> {
    return this.http.get<{ playCount: number }>(`${this.API_URL}/api/playback/stats/user/${userId}/song/${songId}`, { headers: this.getAuthHeaders() });
  }

  getMostPlayedSongs(days: number = 30, limit: number = 10): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/playback/stats/most-played?days=${days}&limit=${limit}`, { headers: this.getAuthHeaders() });
  }

  getDailyPlayStats(userId: number, days: number = 30): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/api/playback/stats/user/${userId}/daily?days=${days}`, { headers: this.getAuthHeaders() });
  }
}
