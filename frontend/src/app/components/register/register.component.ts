// register.component.ts
import {Component, inject} from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {LoginHeaderComponent} from "../login-header/login-header.component";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {


  constructor(private authService: AuthService, private router: Router) {}
  private form= inject(FormBuilder);
  registerData = this.form.nonNullable.group({
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    username: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
  }) ;
  register() {

    this.authService.register(this.registerData).subscribe(
      response => {
        this.authService.saveToken(response.token);
        this.authService.userSig.set(response);
        this.router.navigateByUrl('/');
      },
      error => {
        console.error('Registration failed', error);
      }
    );
  }
}
