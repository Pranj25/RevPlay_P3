import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadService {
  private readonly API_URL = 'http://localhost:9080';

  constructor(private http: HttpClient) {}

  uploadSong(file: File, songData: any): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', songData.title);
    formData.append('artist', songData.artist);
    formData.append('album', songData.album || '');
    formData.append('genre', songData.genre || '');
    formData.append('duration', songData.duration || 0);

    return this.http.post(`${this.API_URL}/api/catalog/songs/upload`, formData);
  }

  uploadSongWithAlbumArtist(
    audioFile: File,
    title: string,
    albumName: string,
    artistName: string,
    genreString: string,
    coverFile: File | null
  ): Observable<any> {
    const formData = new FormData();
    formData.append('audioFile', audioFile);
    formData.append('title', title);
    formData.append('albumName', albumName);
    formData.append('artistName', artistName);
    formData.append('genreString', genreString);
    if (coverFile) {
      formData.append('coverFile', coverFile);
    }

    return this.http.post(`${this.API_URL}/api/catalog/songs/upload`, formData);
  }
}
