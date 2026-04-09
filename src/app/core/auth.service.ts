import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'skyline-mart-auth';
  private readonly loggedInSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  readonly isLoggedIn$ = this.loggedInSubject.asObservable();

  login(email: string, password: string): boolean {
    const ok = email.trim().toLowerCase() === 'demo@gmail.com' && password === 'demo@123';
    if (ok) {
      localStorage.setItem(this.storageKey, 'true');
      this.loggedInSubject.next(true);
    }
    return ok;
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.loggedInSubject.next(false);
  }

  isLoggedIn(): boolean {
    return localStorage.getItem(this.storageKey) === 'true';
  }
}
