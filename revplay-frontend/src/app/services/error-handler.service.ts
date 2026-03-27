import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

export interface ErrorMessage {
  title: string;
  message: string;
  type: 'error' | 'warning' | 'info';
  action?: string;
  actionCallback?: () => void;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  private errorMessages: ErrorMessage[] = [];

  constructor(private router: Router) {}

  // Handle HTTP errors with proper messages and redirects
  handleError(error: any, context?: string): Observable<never> {
    const errorMessage = this.getErrorMessage(error, context);
    this.addError(errorMessage);
    
    // Perform appropriate redirects
    this.handleRedirect(error, context);
    
    return throwError(() => error);
  }

  // Get appropriate error message based on error type
  private getErrorMessage(error: any, context?: string): ErrorMessage {
    if (error instanceof HttpErrorResponse) {
      return this.handleHttpError(error, context);
    }
    
    if (error?.error?.message) {
      return {
        title: 'Error',
        message: error.error.message,
        type: 'error'
      };
    }
    
    if (error?.message) {
      return {
        title: 'Error',
        message: error.message,
        type: 'error'
      };
    }
    
    return this.getDefaultError(error, context);
  }

  // Handle HTTP errors specifically
  private handleHttpError(error: HttpErrorResponse, context?: string): ErrorMessage {
    const status = error.status;
    const errorDetail = error.error;

    switch (status) {
      case 0:
        return this.handleNetworkError(context);
      
      case 400:
        return this.handleBadRequestError(errorDetail, context);
      
      case 401:
        return this.handleUnauthorizedError(context);
      
      case 403:
        return this.handleForbiddenError(errorDetail, context);
      
      case 404:
        return this.handleNotFoundError(errorDetail, context);
      
      case 409:
        return this.handleConflictError(errorDetail, context);
      
      case 422:
        return this.handleValidationError(errorDetail, context);
      
      case 429:
        return this.handleRateLimitError(errorDetail, context);
      
      case 500:
        return this.handleServerError(errorDetail, context);
      
      case 502:
      case 503:
      case 504:
        return this.handleServiceUnavailableError(errorDetail, context);
      
      default:
        return this.handleUnknownError(error, context);
    }
  }

  // Network/Connection errors
  private handleNetworkError(context?: string): ErrorMessage {
    return {
      title: 'Connection Error',
      message: 'Unable to connect to the server. Please check your internet connection and try again.',
      type: 'error',
      action: 'Retry',
      actionCallback: () => window.location.reload()
    };
  }

  // Bad Request errors
  private handleBadRequestError(errorDetail: any, context?: string): ErrorMessage {
    if (context === 'login') {
      return {
        title: 'Login Failed',
        message: errorDetail?.message || 'Invalid login credentials. Please check your username and password.',
        type: 'error'
      };
    }
    
    if (context === 'register') {
      return {
        title: 'Registration Failed',
        message: errorDetail?.message || 'Invalid registration data. Please check all fields and try again.',
        type: 'error'
      };
    }
    
    return {
      title: 'Invalid Request',
      message: errorDetail?.message || 'The request was invalid. Please try again.',
      type: 'error'
    };
  }

  // Unauthorized errors
  private handleUnauthorizedError(context?: string): ErrorMessage {
    return {
      title: 'Authentication Required',
      message: 'You need to be logged in to access this feature.',
      type: 'warning',
      action: 'Login',
      actionCallback: () => this.router.navigate(['/login'])
    };
  }

  // Forbidden errors
  private handleForbiddenError(errorDetail: any, context?: string): ErrorMessage {
    return {
      title: 'Access Denied',
      message: errorDetail?.message || 'You don\'t have permission to access this resource.',
      type: 'error'
    };
  }

  // Not Found errors
  private handleNotFoundError(errorDetail: any, context?: string): ErrorMessage {
    if (context === 'login') {
      return {
        title: 'User Not Found',
        message: 'No account found with these credentials. Please check your username or register for a new account.',
        type: 'error',
        action: 'Register',
        actionCallback: () => this.router.navigate(['/register'])
      };
    }
    
    return {
      title: 'Not Found',
      message: errorDetail?.message || 'The requested resource was not found.',
      type: 'error'
    };
  }

  // Conflict errors
  private handleConflictError(errorDetail: any, context?: string): ErrorMessage {
    if (context === 'register') {
      return {
        title: 'Registration Failed',
        message: errorDetail?.message || 'Username or email already exists. Please try different credentials.',
        type: 'error'
      };
    }
    
    return {
      title: 'Conflict',
      message: errorDetail?.message || 'A conflict occurred with your request.',
      type: 'error'
    };
  }

  // Validation errors
  private handleValidationError(errorDetail: any, context?: string): ErrorMessage {
    const validationErrors = errorDetail?.errors;
    let message = 'Validation failed. Please check your input.';
    
    if (validationErrors && Array.isArray(validationErrors)) {
      message = validationErrors.map((err: any) => err.message || err).join(', ');
    }
    
    return {
      title: 'Validation Error',
      message: message,
      type: 'error'
    };
  }

  // Rate Limit errors
  private handleRateLimitError(errorDetail: any, context?: string): ErrorMessage {
    return {
      title: 'Too Many Requests',
      message: errorDetail?.message || 'Please wait a moment before trying again.',
      type: 'warning'
    };
  }

  // Server errors
  private handleServerError(errorDetail: any, context?: string): ErrorMessage {
    return {
      title: 'Server Error',
      message: errorDetail?.message || 'Something went wrong on our end. Please try again later.',
      type: 'error',
      action: 'Report Issue',
      actionCallback: () => this.reportIssue(errorDetail instanceof Error ? errorDetail : new Error(String(errorDetail)))
    };
  }

  // Service unavailable errors
  private handleServiceUnavailableError(errorDetail: any, context?: string): ErrorMessage {
    return {
      title: 'Service Unavailable',
      message: 'Our services are temporarily unavailable. Please try again in a few minutes.',
      type: 'warning',
      action: 'Retry',
      actionCallback: () => window.location.reload()
    };
  }

  // Unknown errors
  private handleUnknownError(error: any, context?: string): ErrorMessage {
    return {
      title: 'Unexpected Error',
      message: 'An unexpected error occurred. Please try again.',
      type: 'error'
    };
  }

  // Default error
  private getDefaultError(error: any, context?: string): ErrorMessage {
    return {
      title: 'Error',
      message: 'An error occurred. Please try again.',
      type: 'error'
    };
  }

  // Handle appropriate redirects based on error
  private handleRedirect(error: any, context?: string): void {
    if (error instanceof HttpErrorResponse) {
      const status = error.status;
      
      // Redirect to login on authentication errors
      if (status === 401 && context !== 'login') {
        this.router.navigate(['/login']);
      }
      
      // Redirect to register on user not found during login
      if (status === 404 && context === 'login') {
        setTimeout(() => {
          this.router.navigate(['/register']);
        }, 2000);
      }
      
      // Redirect to home on successful login/register (handled in components)
      // This is just for error scenarios
    }
  }

  // Add error to message queue
  private addError(errorMessage: ErrorMessage): void {
    this.errorMessages.push(errorMessage);
    
    // Auto-remove after 10 seconds
    setTimeout(() => {
      this.removeError(errorMessage);
    }, 10000);
  }

  // Remove error from message queue
  removeError(errorMessage: ErrorMessage): void {
    const index = this.errorMessages.indexOf(errorMessage);
    if (index > -1) {
      this.errorMessages.splice(index, 1);
    }
  }

  // Get current error messages
  getErrorMessages(): ErrorMessage[] {
    return [...this.errorMessages];
  }

  // Clear all error messages
  clearErrors(): void {
    this.errorMessages = [];
  }

  // Report issue (placeholder for future implementation)
  private reportIssue(error: any): void {
    console.error('Reporting issue:', error);
    // Future: Send error to logging service
    alert('Issue reported to support team. We\'ll look into it.');
  }

  // Check if API is reachable
  checkApiReachability(): Observable<boolean> {
    // Simple health check to API Gateway
    return new Observable(observer => {
      fetch('http://localhost:9080/health')
        .then(response => {
          observer.next(response.ok);
          observer.complete();
        })
        .catch(error => {
          observer.next(false);
          observer.complete();
        });
    });
  }

  // Show API not reachable message
  showApiNotReachableError(): void {
    const errorMessage: ErrorMessage = {
      title: 'API Not Reachable',
      message: 'Unable to connect to our services. Please check your internet connection or try again later.',
      type: 'error',
      action: 'Retry',
      actionCallback: () => window.location.reload()
    };
    
    this.addError(errorMessage);
  }

  // Handle login-specific errors
  handleLoginError(error: any): void {
    const errorMessage = this.getErrorMessage(error, 'login');
    this.addError(errorMessage);
  }

  // Handle register-specific errors
  handleRegisterError(error: any): void {
    const errorMessage = this.getErrorMessage(error, 'register');
    this.addError(errorMessage);
  }

  // Handle general API errors
  handleApiError(error: any, context?: string): void {
    const errorMessage = this.getErrorMessage(error, context);
    this.addError(errorMessage);
  }
}
