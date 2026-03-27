import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-toast',
  standalone: true,
  template: `
    <div class="toast" [class]="type" [class.show]="show">
      <div class="toast-content">
        <div class="toast-icon">
          <span class="icon">{{ getIcon() }}</span>
        </div>
        <div class="toast-message">
          <h4 *ngIf="title" class="toast-title">{{ title }}</h4>
          <p class="toast-text">{{ message }}</p>
        </div>
        <button class="toast-close" (click)="close()" aria-label="Close">
          ×
        </button>
      </div>
    </div>
  `,
  styles: [`
    .toast {
      position: fixed;
      top: 20px;
      right: 20px;
      min-width: 300px;
      max-width: 400px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      transform: translateX(100%);
      transition: transform 0.3s ease;
      z-index: 10000;
    }
    
    .toast.show {
      transform: translateX(0);
    }
    
    .toast.success {
      border-left: 4px solid #28a745;
    }
    
    .toast.error {
      border-left: 4px solid #dc3545;
    }
    
    .toast.warning {
      border-left: 4px solid #ffc107;
    }
    
    .toast.info {
      border-left: 4px solid #17a2b8;
    }
    
    .toast-content {
      display: flex;
      align-items: flex-start;
      padding: 16px;
    }
    
    .toast-icon {
      margin-right: 12px;
      flex-shrink: 0;
    }
    
    .icon {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      font-size: 12px;
      font-weight: bold;
      color: white;
    }
    
    .toast.success .icon {
      background: #28a745;
    }
    
    .toast.error .icon {
      background: #dc3545;
    }
    
    .toast.warning .icon {
      background: #ffc107;
      color: #212529;
    }
    
    .toast.info .icon {
      background: #17a2b8;
    }
    
    .toast-message {
      flex: 1;
    }
    
    .toast-title {
      margin: 0 0 4px 0;
      font-size: 14px;
      font-weight: 600;
      color: #212529;
    }
    
    .toast-text {
      margin: 0;
      font-size: 13px;
      color: #6c757d;
      line-height: 1.4;
    }
    
    .toast-close {
      background: none;
      border: none;
      font-size: 20px;
      color: #6c757d;
      cursor: pointer;
      padding: 0;
      margin-left: 12px;
      line-height: 1;
    }
    
    .toast-close:hover {
      color: #212529;
    }
  `]
})
export class ToastComponent {
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'info';
  @Input() title: string = '';
  @Input() message: string = '';
  @Input() duration: number = 5000;
  @Output() closeToast = new EventEmitter<void>();

  show: boolean = false;
  private timeoutId: any;

  ngOnInit() {
    this.show = true;
    if (this.duration > 0) {
      this.timeoutId = setTimeout(() => {
        this.close();
      }, this.duration);
    }
  }

  ngOnDestroy() {
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
  }

  getIcon(): string {
    switch (this.type) {
      case 'success': return '✓';
      case 'error': return '✕';
      case 'warning': return '!';
      case 'info': return 'i';
      default: return 'i';
    }
  }

  close() {
    this.show = false;
    this.closeToast.emit();
  }
}
