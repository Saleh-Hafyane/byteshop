// login.component.ts
import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { LoginHeaderComponent } from '../login-header/login-header.component';
import { authGuard } from '../../auth.guard';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink, NgIf],
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  constructor(private authService: AuthService, private router: Router) {}

  protected loginFailed: boolean = false;
  private form = inject(FormBuilder);
  loginData = this.form.nonNullable.group({
    username: new FormControl(''),
    password: new FormControl(''),
  });
  login() {
    if (this.loginData.valid) {
      this.authService.login(this.loginData).subscribe(
        (response) => {
          this.authService.saveToken(response.token);
          this.authService.saveUsername(response.username);
          this.authService.userSig.set(response);
          this.router.navigateByUrl('/');
        },
        (error) => {
          this.loginFailed = true;
        }
      );
    } else {
      this.loginFailed = true;
    }
  }

  protected readonly onsubmit = onsubmit;
}
