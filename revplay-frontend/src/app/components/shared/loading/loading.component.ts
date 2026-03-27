import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loading',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="loading-container" [class.small]="small" [class.inline]="inline">
      <div class="loading-spinner"></div>
      @if (message) {
        <div class="loading-message">{{ message }}</div>
      }
    </div>
  `,
  styleUrls: ['./loading.component.scss']
})
export class LoadingComponent {
  @Input() message: string = 'Loading...';
  @Input() small: boolean = false;
  @Input() inline: boolean = false;
}
