// auth.service.ts
import {Injectable, signal} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import { Router } from '@angular/router';
import {User} from "../common/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  userSig = signal<User|undefined|null>(undefined)
  private apiUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient, private router: Router) {

  }

  register(registerRequest: any): Observable<any> {
    const registerData = registerRequest.getRawValue();
    console.log(registerData)
    return this.http.post<User>(`${this.apiUrl}/register`, registerData);
  }

  login(authRequest: any): Observable<any> {
    const authData = authRequest.getRawValue();
    return this.http.post<User>(`${this.apiUrl}/authenticate`, authData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    this.userSig.set(null);
    this.router.navigate(['/login']);

  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }
  saveUsername(username: string){
    localStorage.setItem('username', username);
  }
  getUsername(){
    return localStorage.getItem('username');
  }
  getToken(){
    return localStorage.getItem('token');
  }
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }
}
