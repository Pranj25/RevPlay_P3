import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, take, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    return this.authService.currentUser().pipe(
      take(1),
      switchMap(user => {
        // First check if user exists and is logged in
        if (user && this.authService.isLoggedIn()) {
          // Validate token with server
          return this.authService.validateToken().pipe(
            map(isValid => {
              if (isValid) {
                return true; // Token valid, allow access
              } else {
                this.router.navigate(['/login']);
                return false; // Token invalid, block access
              }
            })
          );
        } else {
          // No user or not logged in, redirect to login
          this.router.navigate(['/login']);
          return of(false);
        }
      })
    );
  }
}
