import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { RouterLink } from '@angular/router';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule, RouterLink,
    ToolbarModule, ButtonModule, AvatarModule
  ],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  authService = inject(AuthService);
  userEmail: string | null = null;

  constructor() {
    this.userEmail = this.authService.getUserEmail();
  }

  logout(): void {
    this.authService.logout();
  }
}