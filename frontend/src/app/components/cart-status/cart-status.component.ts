import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-cart-status',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './cart-status.component.html',
  styleUrl: './cart-status.component.css'
})
export class CartStatusComponent implements OnInit{
  totalQuantity:number = 0
  totalPrice:number = 0.00
  constructor(private cartService:CartService) {
  }
  ngOnInit() {
    this.updateCartStatus()
  }

  private updateCartStatus() {
    this.cartService.totalQuantity.subscribe(data=>this.totalQuantity  = data)
    this.cartService.totalPrice.subscribe(data=>this.totalPrice  = data)
  }
}
