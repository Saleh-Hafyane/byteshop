// Sends the finished cart to the backend checkout endpoint with the caller's JWT.
import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Purchase} from "../common/purchase";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

// Provided in root; used only by the checkout page.
@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  purchaseUrl = "http://localhost:8080/api/checkout/purchase" // backend checkout endpoint

  constructor(private httpClient:HttpClient,private authService: AuthService) {

  }
  // POST the purchase; the response carries the new order's tracking number.
  makeOrder(purchase:Purchase):Observable<any>{
    const headers = this.authService.getAuthHeaders(); // JWT required — endpoint is role USER
    return this.httpClient.post<Purchase>(this.purchaseUrl,purchase,{headers})
  }
}
