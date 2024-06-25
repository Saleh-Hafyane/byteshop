import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {CartItem} from "../../common/cart-item";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {CheckoutComponent} from "../checkout/checkout.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-cart-detail',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    NgIf,
    RouterLink
  ],
  templateUrl: './cart-detail.component.html',
  styleUrl: './cart-detail.component.css'
})
export class CartDetailComponent implements OnInit{
  cartItems:CartItem[]=[]
  totalQuantity:number = 0
  totalPrice:number = 0.00
  constructor(private cartService: CartService) {
  }
ngOnInit() {
    this.cartDetail()
}

  private cartDetail() {
    this.cartItems = this.cartService.cartItems
    this.cartService.totalQuantity.subscribe(data=>this.totalQuantity = data)
    this.cartService.totalPrice.subscribe(data=>this.totalPrice = data)
    this.cartService.calcCartTotals()

  }

  incQuantity(cartItem: CartItem) {
    this.cartService.addToCart(cartItem)
  }

  decQuantity(cartItem: CartItem) {
    this.cartService.decQuantity(cartItem)

  }

  delQuantity(cartItem: CartItem) {
    this.cartService.remCartItem(cartItem)
  }

  protected readonly CheckoutComponent = CheckoutComponent;
}
