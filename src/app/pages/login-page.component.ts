import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-page">
      <section class="card login-card">
        <div class="login-header">
          <span class="chip">Welcome back</span>
          <h1>Login to Skyline Mart</h1>
          <p>Use the demo credentials below to enter.</p>
        </div>

        <form (ngSubmit)="handleLogin()" class="login-form">
          <label>
            Email
            <input type="email" [(ngModel)]="email" name="email" placeholder="demo@gmail.com" required />
          </label>

          <label>
            Password
            <input type="password" [(ngModel)]="password" name="password" placeholder="demo@123" required />
          </label>

          <button type="submit" class="primary-btn">Login</button>
        </form>

        <div class="login-footer">
          <p><strong>Demo:</strong> <span>demo&#64;gmail.com</span> / <span>demo&#64;123</span></p>
          @if (showError) {
            <p class="error-text">Email or password is incorrect. Try the demo credentials.</p>
          }
        </div>
      </section>
    </div>
  `
})
export class LoginPageComponent {
  email = '';
  password = '';
  showError = false;

  constructor(private authService: AuthService, private router: Router) {}

  handleLogin(): void {
    this.showError = false;
    if (this.authService.login(this.email, this.password)) {
      this.router.navigate(['/products']);
    } else {
      this.showError = true;
    }
  }
}
