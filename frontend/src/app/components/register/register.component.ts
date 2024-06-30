// register.component.ts
import {Component, inject} from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {LoginHeaderComponent} from "../login-header/login-header.component";
import {CustomValidators} from "../../validators/customValidators";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {


  constructor(private authService: AuthService, private router: Router) {}
  private form= inject(FormBuilder);
  registerData = this.form.nonNullable.group({
    firstname: new FormControl("",[Validators.required,Validators.minLength(2),CustomValidators.notOnlySpaces]),
    lastname: new FormControl("",[Validators.required,Validators.minLength(2),CustomValidators.notOnlySpaces]),
    username: new FormControl("",[Validators.required,Validators.minLength(3),CustomValidators.notOnlySpaces]),
    email: new FormControl("",[Validators.required,Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'),CustomValidators.notOnlySpaces]),
    password: new FormControl("",[Validators.required,Validators.minLength(5),CustomValidators.notOnlySpaces]),
  }) ;
  get firstName(){return this.registerData.get('firstname')}
  get lastName(){return this.registerData.get('lastname')}
  get email(){return this.registerData.get('email')}
  get username(){return this.registerData.get('username')}
  get password(){return this.registerData.get('password')}
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
