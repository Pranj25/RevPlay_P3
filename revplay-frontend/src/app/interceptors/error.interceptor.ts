import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle basic network errors
      if (error.status === 0) {
        console.error('Network error:', error);
        showNetworkError();
      }
      
      // Return the error for component-level handling
      return throwError(() => error);
    })
  );
};

// Helper function to show network error
function showNetworkError(): void {
  // Create a simple error message for network issues
  const errorDiv = document.createElement('div');
  errorDiv.className = 'error-message network-error';
  errorDiv.innerHTML = `
    <div class="error-content">
      <div class="error-icon">❌</div>
      <div class="error-text">
        <div class="error-title">Connection Error</div>
        <div class="error-description">Unable to connect to the server. Please check your internet connection.</div>
      </div>
      <button class="error-close" onclick="this.parentElement.parentElement.remove()">×</button>
    </div>
  `;
  
  // Add basic styling
  errorDiv.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: white;
    border: 1px solid #ef4444;
    border-left: 4px solid #ef4444;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    z-index: 1000;
    max-width: 400px;
    animation: slideIn 0.3s ease-out;
  `;
  
  // Add to DOM
  document.body.appendChild(errorDiv);
  
  // Auto-remove after 10 seconds
  setTimeout(() => {
    if (errorDiv.parentNode) {
      errorDiv.remove();
    }
  }, 10000);
}
