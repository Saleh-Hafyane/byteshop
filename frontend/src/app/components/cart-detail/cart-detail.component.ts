// Standalone presentational component: product list with quantity controls and a per-item remove.
import { Component, OnInit } from '@angular/core';
import {CartService} from "../../services/cart.service";
import {CartItem} from "../../common/cart-item";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {CheckoutComponent} from "../checkout/checkout.component";
import {RouterLink} from "@angular/router";
import {ProductService} from "../../services/product.service";
import { ProductImagePipe } from "../../pipes/product-image.pipe";

@Component({
  selector: 'app-cart-detail',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    NgIf,
    RouterLink,
    ProductImagePipe
  ],
  templateUrl: './cart-detail.component.html',
  styleUrl: './cart-detail.component.css',
})
export class CartDetailComponent implements OnInit {
  cartItems:CartItem[]=[]
  totalQuantity:number = 0
  totalPrice:number = 0.00
  // Injected service drives the displayed cart.
  constructor(private cartService: CartService) {
  }
  // Loads the current cart when the component initializes.
  ngOnInit() {
    this.cartDetail()
  }

  private cartDetail() {
    this.cartItems = this.cartService.cartItems
    this.cartService.totalQuantity.subscribe(data=>this.totalQuantity = data)
    this.cartService.totalPrice.subscribe(data=>this.totalPrice = data)
    this.cartService.calcCartTotals()

  }
  // Adds a product to the cart, alerting when it is out of stock.
  incQuantity(cartItem: CartItem) {
    // Attempt to add to cart and reduce units in stock if successful
    if (!this.cartService.addToCart(cartItem)) {
      alert('Product is out of stock or no more available units!');
    }
  }

  decQuantity(cartItem: CartItem) {
    this.cartService.decQuantity(cartItem)

  }

  delQuantity(cartItem: CartItem) {
    this.cartService.remCartItem(cartItem)
  }

  protected readonly CheckoutComponent = CheckoutComponent;
}
