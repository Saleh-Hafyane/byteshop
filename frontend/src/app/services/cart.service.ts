// In-memory shopping cart: items, running totals and stock kept in sync with ProductService.
import { Injectable } from '@angular/core';
import { CartItem } from "../common/cart-item";
import { BehaviorSubject, Subject } from "rxjs";
import { ProductService } from "./product.service";

// One root instance so the header badge and cart page always see the same cart.
@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartItems: CartItem[] = []; // current lines in the cart

  // Broadcast the live totals to the header badge and cart page.
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);

  constructor(private productService: ProductService) {}

  // Adds a unit, but only while stock lasts; returns false when the item is out of stock.
  addToCart(item: CartItem): boolean {
    let availableStock = this.productService.getProductStock(item.id);

    // Stock map may still be empty for a product nobody has clicked yet.
    if (availableStock && availableStock > 0) {
      // Same product added twice just bumps its quantity.
      const cartItem = this.cartItems.find(cartItem => cartItem.id === item.id);
      if (cartItem) {
        cartItem.quantity++;
      } else {
        this.cartItems.push(item);
      }
      // Reserve one unit of stock and refresh the totals.
      this.productService.updateProductStock(item.id, availableStock - 1);
      this.calcCartTotals();
      return true;
    } else {
      // First add: seed the stock map from the product's unitsInStock, minus the one being taken.
      if (!availableStock && availableStock !== 0) {

        this.productService.updateProductStock(item.id, item.unitsInStock - 1);
        this.cartItems.push(item);
      }

      this.calcCartTotals();
      availableStock = this.productService.getProductStock(item.id);

      return availableStock! > 0;
    }
  }

  // Re-sums quantity and price for every line and pushes them to the subjects.
  calcCartTotals() {
    let quantityTotalValue: number = 0;
    let priceTotalValue: number = 0.00;

    // Simple sum over the current lines.
    for (let cartItem of this.cartItems) {
      quantityTotalValue += cartItem.quantity;
      priceTotalValue += cartItem.quantity * cartItem.unitPrice;
    }

    // Price is rounded to cents so 249.97 never shows up as 249.97000000000003.
    this.totalQuantity.next(quantityTotalValue);
    this.totalPrice.next(Math.round(priceTotalValue * 100) / 100);
  }

  // Takes one unit back out of the cart, returning its stock; removes the line at zero.
  decQuantity(cartItem: CartItem) {
    cartItem.quantity--;

    // Put the freed unit back into the shared stock counter.
    const availableStock = this.productService.getProductStock(cartItem.id);
    this.productService.updateProductStock(cartItem.id, availableStock! + 1);

    // Last unit removed means the product leaves the cart entirely.
    if (cartItem.quantity === 0) {
      this.remCartItem(cartItem);
    } else {
      this.calcCartTotals();
    }

  }

  // Drops the line completely and restores the product's full stock count.
  remCartItem(cartItem: CartItem) {
    const cartItemIndex = this.cartItems.findIndex(item => cartItem.id == item.id);

    if (cartItemIndex > -1) {
      this.cartItems.splice(cartItemIndex, 1);

      // Stock goes back to the product's original unitsInStock.
      this.productService.updateProductStock(cartItem.id, cartItem.unitsInStock);
      this.calcCartTotals();
    }
  }
}
