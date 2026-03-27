import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';
import { LoginRequest } from '../../../models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  showPassword = false;
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {
    // Check if user is already logged in
    if (this.authService.isLoggedIn()) {
      console.log('User already logged in, redirecting to home');
      this.router.navigate(['/']);
      return;
    }
    
    // Setup auto-logout timer
    this.authService.setupAutoLogout();
  }
  
  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.markFormAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const loginRequest: LoginRequest = {
      username: this.loginForm.value.username.trim(),
      password: this.loginForm.value.password
    };

    console.log('Attempting login for user:', loginRequest.username);

    this.authService.login(loginRequest).subscribe({
      next: (response: any) => {
        this.isLoading = false;
        console.log('Login successful:', response);
        
        // ✅ LOGIN SUCCESS → HOME
        if (this.authService.isLoggedIn()) {
          console.log('Authentication confirmed, redirecting to home');
          this.router.navigate(['/']);
        } else {
          console.error('Login response received but authentication failed');
          this.errorMessage = 'Login failed. Please try again.';
        }
      },
      error: (error: any) => {
        this.isLoading = false;
        console.error('Login error:', error);
        
        // ✅ USER NOT FOUND → REGISTER (handled by ErrorHandlerService)
        this.errorHandlerService.handleLoginError(error);
      }
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onForgotPassword(): void {
    // TODO: Implement forgot password functionality
    console.log('Forgot password clicked - to be implemented');
  }

  private handleLoginError(error: any): void {
    const status = error.status;
    const errorDetail = error.error;

    switch (status) {
      case 404:
        // User not found - Redirect to register
        this.errorMessage = 'User not found. Please register first.';
        setTimeout(() => {
          this.router.navigate(['/register']);
        }, 2000);
        break;
        
      case 401:
        // Wrong password
        this.errorMessage = 'Incorrect password. Please try again.';
        break;
        
      case 403:
        // Account disabled/blocked
        this.errorMessage = 'Account is disabled. Please contact support.';
        break;
        
      case 500:
        // Server error
        this.errorMessage = 'Server error. Please try again later.';
        break;
        
      default:
        // Generic error or network error
        if (errorDetail?.message) {
          this.errorMessage = errorDetail.message;
        } else if (error.message?.includes('Http failure')) {
          this.errorMessage = 'Network error. Please check your connection.';
        } else {
          this.errorMessage = 'Login failed. Please try again.';
        }
    }
  }

  private markFormAsTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      this.loginForm.get(key)?.markAsTouched();
    });
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
