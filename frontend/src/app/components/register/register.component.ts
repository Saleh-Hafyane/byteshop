// register.component.ts
import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CustomValidators } from '../../validators/customValidators';
import { NgIf } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ApiError } from '../../common/api-error';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgIf],
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private form = inject(FormBuilder);

  errorMessage: string | null = null;
  fieldErrors: Record<string, string> = {};
  isSubmitting: boolean = false;

  registerData = this.form.nonNullable.group({
    firstname: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      CustomValidators.notOnlySpaces,
    ]),
    lastname: new FormControl('', [
      Validators.required,
      Validators.minLength(2),
      CustomValidators.notOnlySpaces,
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      CustomValidators.notOnlySpaces,
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(
        '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
      ),
      CustomValidators.notOnlySpaces,
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      CustomValidators.notOnlySpaces,
    ]),
  });

  get firstName() {
    return this.registerData.get('firstname');
  }
  get lastName() {
    return this.registerData.get('lastname');
  }
  get email() {
    return this.registerData.get('email');
  }
  get username() {
    return this.registerData.get('username');
  }
  get password() {
    return this.registerData.get('password');
  }

  clearFieldError(fieldName: string): void {
    if (this.fieldErrors[fieldName]) {
      delete this.fieldErrors[fieldName];
    }
    // Also clear banner if no other field errors remain or if it was field related
    if (this.errorMessage && Object.keys(this.fieldErrors).length === 0) {
      this.errorMessage = null;
    }
  }

  register(): void {
    this.errorMessage = null;
    this.fieldErrors = {};

    if (this.registerData.invalid) {
      this.registerData.markAllAsTouched();
      this.errorMessage = 'Please fix the validation errors before submitting.';
      return;
    }

    this.isSubmitting = true;

    this.authService.register(this.registerData).subscribe({
      next: (response) => {
        this.isSubmitting = false;
        this.authService.saveToken(response.token);
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

    if (apiError && typeof apiError === 'object') {
      const hasFieldErrors =
        apiError.errors &&
        typeof apiError.errors === 'object' &&
        Object.keys(apiError.errors).length > 0;

      if (hasFieldErrors) {
        this.fieldErrors = { ...apiError.errors };
        // Avoid duplicating field error text in the top banner
        this.errorMessage = null;
      } else {
        this.errorMessage =
          apiError.message || 'Registration failed. Please try again.';
      }
    } else if (typeof error.error === 'string') {
      this.errorMessage = error.error;
    } else {
      this.errorMessage = 'Registration failed. Please try again.';
    }
  }
}

