import { Injectable } from '@angular/core';
import { HttpInterceptorFn, HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const AuthInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  console.log('AuthInterceptor: Processing request to:', req.url);
  
  // ✅ Get JWT token from localStorage
  const token = localStorage.getItem('authToken');
  
  if (token) {
    console.log('AuthInterceptor: Token found, attaching to request');
    
    // ✅ Clone request and attach JWT token
    const cloned = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    
    console.log('AuthInterceptor: Token attached to request');
    return next(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('AuthInterceptor: Request error:', error);
        
        // ✅ Handle authentication errors
        if (error.status === 401 || error.status === 403) {
          console.log('AuthInterceptor: Authentication error, clearing token and redirecting');
          
          // Clear invalid token and user data
          localStorage.removeItem('authToken');
          localStorage.removeItem('currentUser');
          localStorage.removeItem('tokenExpiry');
          localStorage.removeItem('sessionStart');
          
          // Redirect to login
          window.location.href = '/login';
        }
        
        return throwError(() => error);
      })
    );
  } else {
    console.log('AuthInterceptor: No token found, proceeding without auth header');
  }
  
  return next(req);
};
