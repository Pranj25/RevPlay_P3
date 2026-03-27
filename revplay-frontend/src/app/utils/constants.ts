export const API_ENDPOINTS = {
  GATEWAY: 'http://localhost:9080',
  
  // Service routes through API Gateway
  AUTH: 'http://localhost:9080/api/users/auth',
  CATALOG: 'http://localhost:9080/api/catalog',
  PLAYLISTS: 'http://localhost:9080/api/playlists',
  FAVOURITES: 'http://localhost:9080/api/favourites',
  PLAYBACK: 'http://localhost:9080/api/playback',
  ANALYTICS: 'http://localhost:9080/api/analytics'
};

export const ROLES = {
  LISTENER: 'LISTENER',
  ARTIST: 'ARTIST',
  ADMIN: 'ADMIN'
} as const;

export const MUSIC_FORMATS = {
  MP3: 'mp3',
  WAV: 'wav',
  FLAC: 'flac',
  AAC: 'aac',
  OGG: 'ogg'
} as const;

export const SUPPORTED_IMAGE_FORMATS = {
  JPG: 'jpg',
  JPEG: 'jpeg',
  PNG: 'png',
  GIF: 'gif',
  WEBP: 'webp'
} as const;

export const MAX_FILE_SIZES = {
  SONG: 100 * 1024 * 1024, // 100MB
  COVER: 5 * 1024 * 1024   // 5MB
} as const;

export const PAGINATION = {
  DEFAULT_LIMIT: 20,
  MAX_LIMIT: 100,
  DEFAULT_OFFSET: 0
} as const;

export const SEARCH_DEBOUNCE_TIME = 300; // ms

export const LOCAL_STORAGE_KEYS = {
  AUTH_TOKEN: 'authToken',
  CURRENT_USER: 'currentUser',
  LAST_PLAYED_SONG: 'lastPlayedSong',
  VOLUME: 'volume',
  REPEAT_MODE: 'repeatMode',
  SHUFFLE_MODE: 'shuffleMode'
} as const;

export const TOAST_DURATION = {
  SUCCESS: 3000,
  ERROR: 5000,
  WARNING: 4000,
  INFO: 3000
} as const;

export const PLAYER_STATES = {
  PLAYING: 'playing',
  PAUSED: 'paused',
  STOPPED: 'stopped',
  LOADING: 'loading'
} as const;

export const REPEAT_MODES = {
  NONE: 'none',
  ONE: 'one',
  ALL: 'all'
} as const;
