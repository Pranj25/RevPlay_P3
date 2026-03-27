import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class SimpleAuthGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    return this.authService.currentUser().pipe(
      take(1),
      map(user => {
        // Check if user exists and is logged in (local storage validation)
        if (user && this.authService.isLoggedIn()) {
          return true; // Allow access
        } else {
          // Not logged in, redirect to login
          this.router.navigate(['/login']);
          return false; // Block access
        }
      })
    );
  }
}
