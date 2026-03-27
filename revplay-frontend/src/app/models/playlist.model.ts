export interface Playlist {
  id: number;
  name: string;
  description?: string;
  userId: number;
  isPublic: boolean;
  coverImagePath?: string;
  createdAt: string;
  updatedAt: string;
  songCount?: number;
}

export interface PlaylistSong {
  id: number;
  playlistId: number;
  songId: number;
  songTitle: string;
  songArtist: string;
  addedAt: string;
  positionOrder: number;
}

export interface CreatePlaylistRequest {
  name: string;
  description?: string;
  isPublic: boolean;
}

export interface AddSongToPlaylistRequest {
  songId: number;
  songTitle: string;
  songArtist: string;
}
