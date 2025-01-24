// auth.service.ts
import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../common/user';
import { jwtDecode } from 'jwt-decode';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  userSig = signal<User | undefined | null>(undefined);
  private apiUrl = 'http://localhost:8080/api/v1/auth';
  constructor(private http: HttpClient, private router: Router) {}

  // Registers a new user.
  register(registerRequest: any): Observable<any> {
    const registerData = registerRequest.getRawValue();
    return this.http.post<User>(`${this.apiUrl}/register`, registerData);
  }
  // Authenticates a user (login).
  login(authRequest: any): Observable<any> {
    const authData = authRequest.getRawValue();
    return this.http.post<User>(`${this.apiUrl}/authenticate`, authData);
  }
  // Logs out the user by clearing the stored token and username.
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.userSig.set(null);
    this.router.navigate(['/login']);
  }
  // Checks if the user is authenticated.
  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }
  // Saves the token to local storage.
  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }
  // Saves the username to local storage.
  saveUsername(username: string) {
    localStorage.setItem('username', username);
  }

  // Retrieves the username from local storage.
  getUsername() {
    return localStorage.getItem('username');
  }
  // Retrieves the token from local storage.
  getToken() {
    return localStorage.getItem('token');
  }
  // Constructs the HTTP headers with the authorization token.
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    });
  }
  getDecodedToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }

  getRole(): string | null {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = this.getDecodedToken(token);
      return decodedToken ? decodedToken.role : null;
    }
    return null;
  }
}
