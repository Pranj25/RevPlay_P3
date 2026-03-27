export interface Song {
  id: number;
  title: string;
  artist: string;
  album?: string;
  genre?: string;
  lyrics?: string;
  durationSeconds?: number;
  filePath: string;
  fileSize?: number;
  fileFormat?: string;
  coverImagePath?: string;
  uploadedBy: number;
  isPublic: boolean;
  playCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface SongUploadRequest {
  title: string;
  artist?: string;
  album?: string;
  genre?: string;
  lyrics?: string;
  durationSeconds?: number;
  isPublic: boolean;
  musicFile: File;
  coverImage?: File;
}

export interface SongSearchRequest {
  query: string;
  genre?: string;
  artist?: string;
  limit?: number;
  offset?: number;
}
