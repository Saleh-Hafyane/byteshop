import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Purchase} from "../common/purchase";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  purchaseUrl = "http://localhost:8080/api/checkout/purchase"

  constructor(private httpClient:HttpClient,private authService: AuthService) {

  }
  makeOrder(purchase:Purchase):Observable<any>{
    const headers = this.authService.getAuthHeaders();
    return this.httpClient.post<Purchase>(this.purchaseUrl,purchase,{headers})
  }
}
