import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class Validators {
  static requiredMinLength(min: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return { required: true };
      }
      if (control.value.length < min) {
        return { minlength: { requiredLength: min, actualLength: control.value.length } };
      }
      return null;
    };
  }

  static email(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(control.value) ? null : { email: true };
  }

  static password(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return { required: true };
    }
    
    const password = control.value;
    const errors: ValidationErrors = {};
    
    if (password.length < 8) {
      errors['minlength'] = { requiredLength: 8, actualLength: password.length };
    }
    
    if (!/[A-Z]/.test(password)) {
      errors['uppercase'] = true;
    }
    
    if (!/[a-z]/.test(password)) {
      errors['lowercase'] = true;
    }
    
    if (!/[0-9]/.test(password)) {
      errors['number'] = true;
    }
    
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
      errors['special'] = true;
    }
    
    return Object.keys(errors).length > 0 ? errors : null;
  }

  static confirmPassword(passwordControl: AbstractControl): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      
      if (control.value !== passwordControl.value) {
        return { confirmPassword: true };
      }
      
      return null;
    };
  }

  static fileSize(maxSize: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      
      const file = control.value as File;
      if (file.size > maxSize) {
        return { 
          fileSize: { 
            maxSize: maxSize, 
            actualSize: file.size,
            maxSizeFormatted: this.formatFileSize(maxSize),
            actualSizeFormatted: this.formatFileSize(file.size)
          } 
        };
      }
      
      return null;
    };
  }

  static fileType(allowedTypes: string[]): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      
      const file = control.value as File;
      if (!allowedTypes.includes(file.type)) {
        return { 
          fileType: { 
            allowedTypes: allowedTypes,
            actualType: file.type 
          } 
        };
      }
      
      return null;
    };
  }

  static url(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    
    try {
      new URL(control.value);
      return null;
    } catch {
      return { url: true };
    }
  }

  static numeric(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    
    const numericRegex = /^[0-9]+$/;
    return numericRegex.test(control.value) ? null : { numeric: true };
  }

  static positiveNumber(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null;
    }
    
    const num = Number(control.value);
    return num > 0 ? null : { positiveNumber: true };
  }

  static range(min: number, max: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      
      const num = Number(control.value);
      if (num < min || num > max) {
        return { range: { min, max, actual: num } };
      }
      
      return null;
    };
  }

  static pattern(regex: RegExp, errorKey: string = 'pattern'): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }
      
      return regex.test(control.value) ? null : { [errorKey]: { requiredPattern: regex } };
    };
  }

  private static formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }
}
