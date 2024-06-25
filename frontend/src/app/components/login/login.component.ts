// login.component.ts
import {Component, inject} from '@angular/core';
import { AuthService } from '../../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import {FormBuilder, FormControl, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LoginHeaderComponent} from "../login-header/login-header.component";
import {authGuard} from "../../auth.guard";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {


  constructor(private authService: AuthService, private router: Router) {}
  private form= inject(FormBuilder);
  loginData = this.form.nonNullable.group({
    username: new FormControl(''),
    password: new FormControl('')
  });
  login() {
    if (this.loginData.valid) {
      this.authService.login(this.loginData).subscribe(
      response => {
        console.log(response)
        this.authService.saveToken(response.token);
        this.authService.saveUsername(response.username)
        this.authService.userSig.set(response);
        this.router.navigateByUrl('/');
      },
      error => {
        console.error('Login failed', error);
      }
    );
  } else {
    console.error('Form is invalid');
  }
  }
}
