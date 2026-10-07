// login.component.ts
import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NgIf } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../../common/api-error';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink, NgIf],
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private form = inject(FormBuilder);

  errorMessage: string | null = null;
  isSubmitting: boolean = false;

  loginData = this.form.nonNullable.group({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  get username() {
    return this.loginData.get('username');
  }

  get password() {
    return this.loginData.get('password');
  }

  clearError(): void {
    if (this.errorMessage) {
      this.errorMessage = null;
    }
  }

  login(): void {
    this.errorMessage = null;

    if (this.loginData.invalid) {
      this.loginData.markAllAsTouched();
      this.errorMessage = 'Please enter both username and password.';
      return;
    }

    this.isSubmitting = true;

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.authService.saveToken(response.token);
        this.authService.saveUsername(response.username);
        this.authService.userSig.set(response);
        this.router.navigateByUrl('/');
      },
      error: (error: HttpErrorResponse) => {
        this.isSubmitting = false;
        this.handleError(error);
      },
    });
  }

  private handleError(error: HttpErrorResponse): void {
    if (error.status === 0) {
      this.errorMessage =
        'Unable to reach server. Please check your connection and try again.';
      return;
    }
    
    const apiError = error.error as ApiError | undefined;
    if (apiError && typeof apiError === 'object' && apiError.message) {
      this.errorMessage = apiError.message;
    } else if (typeof error.error === 'string') {
      this.errorMessage = error.error;
    } else if (error.status === 401) {
      this.errorMessage = 'Invalid username or password.';
    } else {
      this.errorMessage = 'Login failed. Please try again.';
    }
  }
}

