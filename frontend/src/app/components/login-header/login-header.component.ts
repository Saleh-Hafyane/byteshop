import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import {RegisterComponent} from "../register/register.component";
import {authGuard} from "../../auth.guard";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-login-header',
  standalone: true,
  imports: [
    RouterLink,
    NgIf
  ],
  templateUrl: './login-header.component.html',
  styleUrl: './login-header.component.css'
})
export class LoginHeaderComponent implements OnInit{
  authService = inject(AuthService)

  constructor() {
  }
  ngOnInit(): void {
    this.unAuthDefault()
  }

  logout(){
    this.authService.logout();


  }
  unAuthDefault(){
    if (this.authService.getUsername()===null){
      this.authService.userSig.set(null);

    }
  }
  getUser(){
    let username = this.authService.userSig()?.username
    if(username===undefined){
      username = this.authService.getUsername()

      }

    return username
  }

  isAuth(){
    return this.authService.isAuthenticated()
  }




}
