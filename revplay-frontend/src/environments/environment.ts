export const environment = {
  production: false,
  apiUrl: 'http://localhost:9080',
  apiTimeout: 10000,
  enableDebug: true,
  enableAnalytics: false,
  version: '1.0.0',
  
  // Single backend entry point - API Gateway
  gateway: 'http://localhost:9080',
  
  // Service routes through API Gateway
  services: {
    auth: 'http://localhost:9080/api/users/auth',
    catalog: 'http://localhost:9080/api/catalog',
    playlists: 'http://localhost:9080/api/playlists',
    favourites: 'http://localhost:9080/api/favourites',
    playback: 'http://localhost:9080/api/playback',
    analytics: 'http://localhost:9080/api/analytics'
  },
  
  // Feature flags
  features: {
    enableUpload: true,
    enableSocial: false,
    enableLiveStreaming: false,
    enableOfflineMode: false,
    enablePushNotifications: false
  },
  
  // File upload limits
  upload: {
    maxSongSize: 100 * 1024 * 1024, // 100MB
    maxCoverSize: 5 * 1024 * 1024,   // 5MB
    acceptedAudioFormats: ['mp3', 'wav', 'flac', 'aac', 'ogg'],
    acceptedImageFormats: ['jpg', 'jpeg', 'png', 'gif', 'webp']
  },
  
  // Player settings
  player: {
    defaultVolume: 0.8,
    autoPlay: false,
    shuffle: false,
    repeat: 'none'
  },
  
  // UI settings
  ui: {
    theme: 'light',
    language: 'en',
    pageSize: 20,
    debounceTime: 300
  }
};
