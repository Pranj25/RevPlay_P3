export const environment = {
  production: true,
  apiUrl: 'https://api.revplay.com',
  apiTimeout: 15000,
  enableDebug: false,
  enableAnalytics: true,
  version: '1.0.0',
  
  // Single backend entry point - API Gateway
  gateway: 'https://api.revplay.com',
  
  // Service routes through API Gateway
  services: {
    auth: 'https://api.revplay.com/api/users/auth',
    catalog: 'https://api.revplay.com/api/catalog',
    playlists: 'https://api.revplay.com/api/playlists',
    favourites: 'https://api.revplay.com/api/favourites',
    playback: 'https://api.revplay.com/api/playback',
    analytics: 'https://api.revplay.com/api/analytics'
  },
  
  // Feature flags
  features: {
    enableUpload: true,
    enableSocial: true,
    enableLiveStreaming: true,
    enableOfflineMode: true,
    enablePushNotifications: true
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
    theme: 'dark',
    language: 'en',
    pageSize: 20,
    debounceTime: 300
  }
};
