import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class RouteProtectionService {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Check if user can access route
  canAccessRoute(route: string): boolean {
    const isProtectedRoute = this.isProtectedRoute(route);
    
    if (isProtectedRoute) {
      return this.authService.isLoggedIn();
    }
    
    return true; // Public routes are always accessible
  }

  // Check if route requires authentication
  private isProtectedRoute(route: string): boolean {
    const protectedRoutes = [
      '',
      'home',
      'playlists',
      'favorites',
      'profile',
      'settings',
      'upload-song',
      'history'
    ];
    
    // Check exact match or starts with protected route
    return protectedRoutes.some(protectedRoute => 
      route === protectedRoute || route.startsWith(protectedRoute + '/')
    );
  }

  // Redirect to appropriate page based on auth status
  redirectToAppropriatePage(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    } else {
      this.router.navigate(['/login']);
    }
  }

  // Protect route with automatic redirect
  protectRoute(targetRoute: string): boolean {
    if (!this.canAccessRoute(targetRoute)) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }

  // Check if user has required role for route
  hasRequiredRole(route: string, requiredRole?: string): boolean {
    if (!requiredRole) {
      return this.canAccessRoute(route);
    }
    
    const isProtectedRoute = this.isProtectedRoute(route);
    if (!isProtectedRoute) {
      return true;
    }
    
    return this.authService.isLoggedIn() && this.authService.hasRole(requiredRole);
  }

  // Get accessible routes for current user
  getAccessibleRoutes(): string[] {
    const allRoutes = [
      { path: '/', name: 'Home', protected: true },
      { path: '/login', name: 'Login', protected: false },
      { path: '/register', name: 'Register', protected: false },
      { path: '/songs', name: 'Songs', protected: false },
      { path: '/artists', name: 'Artists', protected: false },
      { path: '/albums', name: 'Albums', protected: false },
      { path: '/search', name: 'Search', protected: false },
      { path: '/playlists', name: 'Playlists', protected: true },
      { path: '/favorites', name: 'Favorites', protected: true },
      { path: '/profile', name: 'Profile', protected: true },
      { path: '/settings', name: 'Settings', protected: true },
      { path: '/upload-song', name: 'Upload Song', protected: true, role: 'ARTIST' },
      { path: '/history', name: 'History', protected: true }
    ];
    
    return allRoutes
      .filter(route => {
        if (!route.protected) {
          return true;
        }
        
        if (route.role) {
          return this.authService.hasRole(route.role);
        }
        
        return this.authService.isLoggedIn();
      })
      .map(route => route.path);
  }

  // Security check for navigation
  performSecurityCheck(): void {
    // Check if token is expired
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      this.router.navigate(['/login']);
      return;
    }

    // Check if token is expiring soon
    if (this.authService.isTokenExpiringSoon()) {
      // Optionally refresh token or show warning
      console.warn('Token is expiring soon');
    }
  }

  // Initialize route protection
  initializeProtection(): void {
    // Set up periodic security checks
    setInterval(() => {
      this.performSecurityCheck();
    }, 60000); // Check every minute
  }

  // Get current protection status
  getProtectionStatus(): any {
    return {
      isLoggedIn: this.authService.isLoggedIn(),
      currentUser: this.authService.getCurrentUserValue(),
      tokenRemainingTime: this.authService.getTokenRemainingTime(),
      isTokenExpiringSoon: this.authService.isTokenExpiringSoon(),
      accessibleRoutes: this.getAccessibleRoutes()
    };
  }
}
