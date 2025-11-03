import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedInSignal = signal<boolean>(this.isLoggedIn());

  constructor(private router: Router) { }

  login(email: string): void {
    const mockToken = btoa(email + new Date().getTime());
    
    localStorage.setItem('authToken', mockToken);
    localStorage.setItem('userEmail', email);
    
    this.loggedInSignal.set(true);
    
    this.router.navigate(['/departamentos']);
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
    
    this.loggedInSignal.set(false);
    
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('authToken');
  }

  getUserEmail(): string | null {
    return localStorage.getItem('userEmail');
  }
}