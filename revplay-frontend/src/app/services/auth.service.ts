import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, of, timer } from 'rxjs';
import { map, tap, catchError, switchMap } from 'rxjs/operators';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../models/user.model';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private readonly API_URL = 'http://localhost:9080';
  private readonly TOKEN_KEY = 'authToken';
  private readonly USER_KEY = 'currentUser';
  private readonly TOKEN_EXPIRY_KEY = 'tokenExpiry';
  private readonly SESSION_KEY = 'sessionStart';
  
  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.initializeAuthFromStorage();
    this.setupSessionTimer();
  }

  private initializeAuthFromStorage(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return; // Skip localStorage access on server
    }
    
    const token = localStorage.getItem(this.TOKEN_KEY);
    const user = localStorage.getItem(this.USER_KEY);
    const tokenExpiry = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
    
    if (token && user && tokenExpiry) {
      const expiryTime = parseInt(tokenExpiry);
      const currentTime = Date.now();
      
      // Check if token is still valid
      if (currentTime < expiryTime) {
        this.currentUserSubject.next(JSON.parse(user));
        // Update session start time
        localStorage.setItem(this.SESSION_KEY, currentTime.toString());
      } else {
        // Token expired, clear storage
        this.clearStorage();
      }
    }
  }

  private setupSessionTimer(): void {
    // Check token expiry every minute
    timer(0, 60000).subscribe(() => {
      this.checkTokenExpiry();
    });
  }

  private checkTokenExpiry(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    
    const tokenExpiry = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
    if (tokenExpiry) {
      const expiryTime = parseInt(tokenExpiry);
      const currentTime = Date.now();
      
      if (currentTime >= expiryTime) {
        // Token expired, logout user
        this.logout();
      }
    }
  }

  currentUser(): Observable<User | null> {
    return this.currentUserSubject.asObservable();
  }

  getCurrentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  get token(): string | null {
    if (!isPlatformBrowser(this.platformId)) {
      return null;
    }
    
    const token = localStorage.getItem(this.TOKEN_KEY);
    const tokenExpiry = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
    
    if (token && tokenExpiry) {
      const expiryTime = parseInt(tokenExpiry);
      const currentTime = Date.now();
      
      // Return token only if not expired
      return currentTime < expiryTime ? token : null;
    }
    
    return null;
  }

  private setTokenWithExpiry(token: string, expiresIn: number): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    
    const expiryTime = Date.now() + (expiresIn * 1000); // Convert to milliseconds
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.TOKEN_EXPIRY_KEY, expiryTime.toString());
    localStorage.setItem(this.SESSION_KEY, Date.now().toString());
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/api/users/auth/login`, loginRequest)
      .pipe(
        tap(response => {
          if (response.token && response.user) {
            // Store token with expiry
            this.setTokenWithExpiry(response.token, response.expiresIn);
            
            // Store user data
            if (isPlatformBrowser(this.platformId)) {
              localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
            }
            
            // Update behavior subject
            this.currentUserSubject.next(response.user);
          }
        }),
        catchError(error => {
          console.error('Login error:', error);
          throw error;
        })
      );
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/api/users/auth/register`, registerRequest)
      .pipe(
        catchError(error => {
          console.error('Registration error:', error);
          throw error;
        })
      );
  }

  logout(): void {
    this.clearStorage();
    this.currentUserSubject.next(null);
  }

  private clearStorage(): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.TOKEN_EXPIRY_KEY);
    localStorage.removeItem(this.SESSION_KEY);
  }

  validateToken(): Observable<boolean> {
    const token = this.token;
    if (!token) {
      return of(false);
    }
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    
    return this.http.post(`${this.API_URL}/api/users/auth/validate`, {}, { headers })
      .pipe(
        map(() => true),
        catchError(() => {
          // Token invalid, clear storage and return false
          this.logout();
          return of(false);
        })
      );
  }

  refreshToken(): Observable<AuthResponse> {
    const token = this.token;
    if (!token) {
      return new Observable(observer => observer.error('No token found'));
    }
    
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    
    return this.http.post<AuthResponse>(`${this.API_URL}/api/users/auth/refresh`, {}, { headers })
      .pipe(
        tap(response => {
          if (response.token) {
            // Update token with new expiry
            this.setTokenWithExpiry(response.token, response.expiresIn);
          }
        }),
        catchError(error => {
          // Refresh failed, logout user
          this.logout();
          throw error;
        })
      );
  }

  isArtist(): boolean {
    const user = this.getCurrentUserValue();
    return user?.role === 'ARTIST';
  }

  isAdmin(): boolean {
    const user = this.getCurrentUserValue();
    return user?.role === 'ADMIN';
  }

  isLoggedIn(): boolean {
    return !!this.token && !!this.getCurrentUserValue();
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUserValue();
    return user?.role === role;
  }

  getSessionDuration(): number {
    if (!isPlatformBrowser(this.platformId)) {
      return 0;
    }
    
    const sessionStart = localStorage.getItem(this.SESSION_KEY);
    if (sessionStart) {
      return Date.now() - parseInt(sessionStart);
    }
    return 0;
  }

  getTokenRemainingTime(): number {
    if (!isPlatformBrowser(this.platformId)) {
      return 0;
    }
    
    const tokenExpiry = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
    if (tokenExpiry) {
      return Math.max(0, parseInt(tokenExpiry) - Date.now());
    }
    return 0;
  }

  isTokenExpiringSoon(minutes: number = 5): boolean {
    const remainingTime = this.getTokenRemainingTime();
    const fiveMinutesInMs = minutes * 60 * 1000;
    return remainingTime > 0 && remainingTime <= fiveMinutesInMs;
  }

  // Auto-logout when token expires
  setupAutoLogout(): void {
    const remainingTime = this.getTokenRemainingTime();
    if (remainingTime > 0) {
      timer(remainingTime).subscribe(() => {
        this.logout();
      });
    }
  }
}
