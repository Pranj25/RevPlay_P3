import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, timer } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private sessionActiveSubject = new BehaviorSubject<boolean>(false);
  private sessionWarningSubject = new BehaviorSubject<boolean>(false);
  private sessionTimeLeftSubject = new BehaviorSubject<number>(0);
  
  private readonly WARNING_TIME = 5 * 60 * 1000; // 5 minutes in milliseconds
  private sessionTimer: any;
  private warningTimer: any;

  constructor(private authService: AuthService) {
    this.initializeSession();
  }

  private initializeSession(): void {
    // Check session status every second
    timer(0, 1000).subscribe(() => {
      this.updateSessionStatus();
    });
  }

  private updateSessionStatus(): void {
    const isLoggedIn = this.authService.isLoggedIn();
    const tokenRemainingTime = this.authService.getTokenRemainingTime();
    
    this.sessionActiveSubject.next(isLoggedIn);
    this.sessionTimeLeftSubject.next(tokenRemainingTime);
    
    // Show warning when token is expiring soon
    const shouldShowWarning = isLoggedIn && this.authService.isTokenExpiringSoon(5);
    this.sessionWarningSubject.next(shouldShowWarning);
  }

  // Observable getters
  get sessionActive$(): Observable<boolean> {
    return this.sessionActiveSubject.asObservable();
  }

  get sessionWarning$(): Observable<boolean> {
    return this.sessionWarningSubject.asObservable();
  }

  get sessionTimeLeft$(): Observable<number> {
    return this.sessionTimeLeftSubject.asObservable();
  }

  // Session utility methods
  getSessionDuration(): string {
    const duration = this.authService.getSessionDuration();
    return this.formatDuration(duration);
  }

  getTimeRemaining(): string {
    const timeLeft = this.authService.getTokenRemainingTime();
    return this.formatDuration(timeLeft);
  }

  private formatDuration(milliseconds: number): string {
    const seconds = Math.floor(milliseconds / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    
    if (hours > 0) {
      return `${hours}h ${minutes % 60}m`;
    } else if (minutes > 0) {
      return `${minutes}m ${seconds % 60}s`;
    } else {
      return `${seconds}s`;
    }
  }

  // Session management
  extendSession(): Observable<any> {
    return this.authService.refreshToken();
  }

  endSession(): void {
    this.authService.logout();
  }

  // Session status checks
  isSessionActive(): boolean {
    return this.sessionActiveSubject.value;
  }

  isSessionExpiringSoon(): boolean {
    return this.sessionWarningSubject.value;
  }

  getTimeLeftInSeconds(): number {
    return Math.floor(this.sessionTimeLeftSubject.value / 1000);
  }

  // Auto-logout setup
  setupAutoLogout(): void {
    this.authService.setupAutoLogout();
  }

  // Session analytics
  getSessionInfo(): any {
    return {
      isActive: this.isSessionActive(),
      isExpiringSoon: this.isSessionExpiringSoon(),
      timeLeft: this.getTimeRemaining(),
      duration: this.getSessionDuration(),
      user: this.authService.getCurrentUserValue()
    };
  }
}
