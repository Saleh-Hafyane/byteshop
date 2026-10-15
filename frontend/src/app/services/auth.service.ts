// Login/register calls plus everything the app stores about the current session.
import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../common/user';
import { jwtDecode } from 'jwt-decode';
// Provided in root so every component shares the same session state.
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  userSig = signal<User | undefined | null>(undefined); // live login state read by the header
  private apiUrl = 'http://localhost:8080/api/v1/auth'; // backend auth endpoints
  constructor(private http: HttpClient, private router: Router) {}

  // POST /register — takes the raw form values and returns the new session.
  register(registerRequest: any): Observable<any> {
    const registerData = registerRequest.getRawValue();
    return this.http.post<User>(`${this.apiUrl}/register`, registerData);
  }
  // POST /authenticate — checks credentials and returns token, username and role.
  login(authRequest: any): Observable<any> {
    const authData = authRequest.getRawValue();
    return this.http.post<User>(`${this.apiUrl}/authenticate`, authData);
  }
  // Forgets the session locally and returns to the login page.
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.userSig.set(null);
    this.router.navigate(['/login']);
  }
  // True whenever a token exists in localStorage.
  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }
  // Persists the JWT for later requests.
  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }
  // Persists the display name for the header.
  saveUsername(username: string) {
    localStorage.setItem('username', username);
  }

  // Reads the stored display name, or null when logged out.
  getUsername() {
    return localStorage.getItem('username');
  }
  // Reads the stored JWT, or null when logged out.
  getToken() {
    return localStorage.getItem('token');
  }
  // Builds headers with the JWT for endpoints the interceptor doesn't cover.
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    });
  }
  // Parses the JWT payload without verifying it; null when the token is garbage.
  getDecodedToken(token: string): any {
    try {
      return jwtDecode(token);
    } catch (error) {
      console.error('Error decoding JWT:', error);
      return null;
    }
  }

  // Reads the role claim from the stored token to decide admin-only UI.
  getRole(): string | null {
    const token = localStorage.getItem('token');
    if (token) {
      const decodedToken = this.getDecodedToken(token);
      return decodedToken ? decodedToken.role : null;
    }
    return null;
  }
}
