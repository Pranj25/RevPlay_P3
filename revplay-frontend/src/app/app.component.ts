import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LayoutComponent } from './components/core/layout/layout.component';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LayoutComponent],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'RevPlay';

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.initAuth();
  }
}
