import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanActivate {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    return this.authService.currentUser().pipe(
      take(1),
      map(user => {
        // If user is already logged in, redirect to home
        if (user && this.authService.isLoggedIn()) {
          this.router.navigate(['/']);
          return false;
        }
        // If user is not logged in, allow access to login/register
        return true;
      })
    );
  }
}
