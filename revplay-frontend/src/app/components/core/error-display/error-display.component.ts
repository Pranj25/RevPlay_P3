import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { ErrorHandlerService, ErrorMessage } from '../../../services/error-handler.service';

@Component({
  selector: 'app-error-display',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './error-display.component.html',
  styleUrls: ['./error-display.component.scss']
})
export class ErrorDisplayComponent implements OnInit, OnDestroy {
  errorMessages: ErrorMessage[] = [];
  private subscription: Subscription | null = null;

  constructor(private errorHandlerService: ErrorHandlerService) {}

  ngOnInit() {
    // Subscribe to error messages
    this.subscription = new Subscription();
    this.updateErrorMessages();
    
    // Check for errors every second
    const interval = setInterval(() => {
      this.updateErrorMessages();
    }, 1000);
    
    this.subscription.add(() => clearInterval(interval));
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  private updateErrorMessages(): void {
    this.errorMessages = this.errorHandlerService.getErrorMessages();
  }

  removeError(error: ErrorMessage): void {
    this.errorHandlerService.removeError(error);
  }

  executeErrorAction(error: ErrorMessage): void {
    if (error.actionCallback) {
      error.actionCallback();
    }
    this.removeError(error);
  }

  getErrorIcon(type: string): string {
    switch (type) {
      case 'error':
        return '❌';
      case 'warning':
        return '⚠️';
      case 'info':
        return 'ℹ️';
      default:
        return '❌';
    }
  }

  getErrorClass(type: string): string {
    return `error-${type}`;
  }
}
