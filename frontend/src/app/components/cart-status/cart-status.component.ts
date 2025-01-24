import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cart-status',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './cart-status.component.html',
  styleUrl: './cart-status.component.css',
})
export class CartStatusComponent implements OnInit {
  totalQuantity: number = 0;
  totalPrice: number = 0.0;
  constructor(
    private cartService: CartService,
    private authService: AuthService
  ) {}
  ngOnInit() {
    this.updateCartStatus();
  }

  private updateCartStatus() {
    this.cartService.totalQuantity.subscribe(
      (data) => (this.totalQuantity = data)
    );
    this.cartService.totalPrice.subscribe((data) => (this.totalPrice = data));
  }
  getRole() {
    return this.authService.getRole();
  }
}
