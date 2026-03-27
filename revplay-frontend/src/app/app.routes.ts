import { Routes } from '@angular/router';
import { HomeComponent } from './components/core/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { SongsComponent } from './components/music/songs/songs.component';
import { ArtistsComponent } from './components/music/artists/artists.component';
import { ArtistDetailComponent } from './components/music/artist-detail/artist-detail.component';
import { AlbumsComponent } from './components/music/albums/albums.component';
import { AlbumDetailComponent } from './components/music/album-detail/album-detail.component';
import { PlaylistsComponent } from './components/music/playlists/playlists.component';
import { PlaylistDetailComponent } from './components/music/playlist-detail/playlist-detail.component';
import { ProfileComponent } from './components/core/profile/profile.component';
import { SettingsComponent } from './components/core/settings/settings.component';
import { UploadSongComponent } from './components/music/upload-song/upload-song.component';
import { HistoryComponent } from './components/music/history/history.component';
import { FavoritesComponent } from './components/music/favorites/favorites.component';
import { SearchComponent } from './components/music/search/search.component';
import { ErrorComponent } from './components/core/error/error.component';
import { TestingDashboardComponent } from './components/core/testing-dashboard/testing-dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { SimpleAuthGuard } from './guards/simple-auth.guard';
import { NoAuthGuard } from './guards/no-auth.guard';

export const routes: Routes = [
  // Home page - DEFAULT route (protected with simple guard for fast validation)
  { path: '', component: HomeComponent, canActivate: [SimpleAuthGuard] },
  
  // Authentication pages
  { path: 'login', component: LoginComponent, canActivate: [NoAuthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [NoAuthGuard] },
  
  // Music pages - public access
  { path: 'songs', component: SongsComponent },
  { path: 'artists', component: ArtistsComponent },
  { path: 'artists/:name', component: ArtistDetailComponent },
  { path: 'albums', component: AlbumsComponent },
  { path: 'albums/:name', component: AlbumDetailComponent },
  { path: 'search', component: SearchComponent },
  
  // Protected pages - require authentication
  { path: 'playlists', component: PlaylistsComponent, canActivate: [AuthGuard] },
  { path: 'playlists/:id', component: PlaylistDetailComponent, canActivate: [AuthGuard] },
  { path: 'favorites', component: FavoritesComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard] },
  { path: 'upload-song', component: UploadSongComponent, canActivate: [AuthGuard] },
  { path: 'history', component: HistoryComponent, canActivate: [AuthGuard] },
  
  // Wildcard - redirect to login for unknown routes
  { path: '**', redirectTo: '/login' },

  // Testing Dashboard (for development/testing)
  { path: 'testing', component: TestingDashboardComponent }
];
