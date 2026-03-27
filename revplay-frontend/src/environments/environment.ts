export const environment = {
  production: false,
  apiUrl: 'https://revplay-api-gateway.onrender.com',
  apiTimeout: 10000,
  enableDebug: true,
  enableAnalytics: false,
  version: '1.0.0',
  
  // Single backend entry point - API Gateway
  gateway: 'https://revplay-api-gateway.onrender.com',
  
  // Service routes through API Gateway
  services: {
    auth: 'https://revplay-api-gateway.onrender.com/api/users/auth',
    catalog: 'https://revplay-api-gateway.onrender.com/api/catalog',
    playlists: 'https://revplay-api-gateway.onrender.com/api/playlists',
    favourites: 'https://revplay-api-gateway.onrender.com/api/favourites',
    playback: 'https://revplay-api-gateway.onrender.com/api/playback',
    analytics: 'https://revplay-api-gateway.onrender.com/api/analytics'
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
