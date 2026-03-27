import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { ErrorHandlerService } from '../../../services/error-handler.service';
import { RegisterRequest } from '../../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  isLoading = false;
  successMessage = '';
  errorMessage = '';
  showPassword = false;
  showConfirmPassword = false;
  
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern('^[a-zA-Z0-9_]+$')]],
      email: ['', [Validators.required, Validators.email, Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(50), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,}$')]],
      confirmPassword: ['', [Validators.required]],
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[a-zA-Z\\s]+$')]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[a-zA-Z\\s]+$')]],
      role: ['LISTENER', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Check if user is already logged in
    if (this.authService.isLoggedIn()) {
      console.log('User already logged in, redirecting to home');
      this.router.navigate(['/']);
      return;
    }
  }
  
  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.markFormAsTouched();
      return;
    }

    this.isLoading = true;
    this.successMessage = '';
    this.errorMessage = '';

    const registerRequest: RegisterRequest = {
      username: this.registerForm.value.username.trim(),
      email: this.registerForm.value.email.trim().toLowerCase(),
      password: this.registerForm.value.password,
      confirmPassword: this.registerForm.value.confirmPassword,
      firstName: this.registerForm.value.firstName.trim(),
      lastName: this.registerForm.value.lastName.trim(),
      role: this.registerForm.value.role
    };

    console.log('Attempting registration for user:', registerRequest.username);

    this.authService.register(registerRequest).subscribe({
      next: (response: any) => {
        this.isLoading = false;
        console.log('Registration successful:', response);
        
        // ✅ REGISTER SUCCESS → LOGIN
        this.successMessage = 'Registration successful! Redirecting to login...';
        
        setTimeout(() => {
          console.log('Redirecting to login for authentication');
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error: any) => {
        this.isLoading = false;
        console.error('Registration error:', error);
        
        this.errorHandlerService.handleRegisterError(error);
      }
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  onLoginInstead(): void {
    console.log('User wants to login instead');
    this.router.navigate(['/login']);
  }

  private handleRegistrationError(error: any): void {
    const status = error.status;
    const errorDetail = error.error;

    switch (status) {
      case 409:
        // Username or email already exists
        this.errorMessage = 'Username or email already exists. Please try different credentials.';
        break;
        
      case 400:
        // Bad request - validation errors
        if (errorDetail?.message) {
          this.errorMessage = errorDetail.message;
        } else {
          this.errorMessage = 'Invalid input. Please check all fields and try again.';
        }
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
          this.errorMessage = 'Registration failed. Please try again.';
        }
    }
  }

  private passwordMatchValidator(form: FormGroup): { [key: string]: boolean } | null {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    
    return null;
  }

  private markFormAsTouched(): void {
    Object.keys(this.registerForm.controls).forEach(key => {
      this.registerForm.get(key)?.markAsTouched();
    });
  }

  // Getters for form controls
  get username() {
    return this.registerForm.get('username');
  }

  get email() {
    return this.registerForm.get('email');
  }

  get password() {
    return this.registerForm.get('password');
  }

  get confirmPassword() {
    return this.registerForm.get('confirmPassword');
  }

  get firstName() {
    return this.registerForm.get('firstName');
  }

  get lastName() {
    return this.registerForm.get('lastName');
  }

  get role() {
    return this.registerForm.get('role');
  }
}
