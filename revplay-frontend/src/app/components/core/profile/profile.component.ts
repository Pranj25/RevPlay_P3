import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any;

  constructor(public authService: AuthService) {}
  
  ngOnInit() {
    this.user = this.authService.getCurrentUserValue();
  }
  
  getMemberSince(): string {
    return new Date().toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'long' 
    });
  }
}
