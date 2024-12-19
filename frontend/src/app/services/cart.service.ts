import { Injectable } from '@angular/core';
import { CartItem } from "../common/cart-item";
import { BehaviorSubject, Subject } from "rxjs";
import { ProductService } from "./product.service";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartItems: CartItem[] = [];

  // Observables to track total quantity and total price in the cart
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);
  totalPrice: Subject<number> = new BehaviorSubject<number>(0);

  constructor(private productService: ProductService) {}

  /**
   * Adds an item to the cart.
   * Checks if there is available stock before adding or updating the item.
   */
  addToCart(item: CartItem): boolean {
    let availableStock = this.productService.getProductStock(item.id);

    // Check if stock is available for the item
    if (availableStock && availableStock > 0) {
      // Find if the item is already in the cart
      const cartItem = this.cartItems.find(cartItem => cartItem.id === item.id);
      // Increase quantity if item is already in cart, otherwise add new item
      if (cartItem) {
        cartItem.quantity++;
      } else {
        this.cartItems.push(item);
      }
      // Decrease stock in ProductService and synchronize
      this.productService.updateProductStock(item.id, availableStock - 1);
      this.calcCartTotals();
      return true;
    } else {
      // If fixes: when first adding the item to the cart, "availableStock" is undefined
      if (!availableStock && availableStock !== 0) {

        this.productService.updateProductStock(item.id, item.unitsInStock - 1);
        this.cartItems.push(item);
      }

      this.calcCartTotals();
      availableStock = this.productService.getProductStock(item.id);

      return availableStock! > 0;
    }
  }

  /**
   * Calculates total quantity and price of all items in the cart.
   * Updates the `totalQuantity` and `totalPrice` subjects.
   */
  calcCartTotals() {
    let quantityTotalValue: number = 0;
    let priceTotalValue: number = 0.00;

    // Calculate total quantity and price based on cart items
    for (let cartItem of this.cartItems) {
      quantityTotalValue += cartItem.quantity;
      priceTotalValue += cartItem.quantity * cartItem.unitPrice;
    }

    // Emit new totals to subscribers
    this.totalQuantity.next(quantityTotalValue);
    this.totalPrice.next(priceTotalValue);
  }

  /**
   * Decreases the quantity of a specific item in the cart.
   * If quantity reaches zero, removes the item from the cart.
   */
  decQuantity(cartItem: CartItem) {
    // Reduce quantity in the cart
    cartItem.quantity--;

    // Get the current stock of the product and increase it
    const availableStock = this.productService.getProductStock(cartItem.id);
    this.productService.updateProductStock(cartItem.id, availableStock! + 1);

    // Remove the cart item if quantity reaches zero, else recalculate totals
    if (cartItem.quantity === 0) {
      this.remCartItem(cartItem);
    } else {
      this.calcCartTotals();
    }

  }

  /**
   * Removes a specific item from the cart.
   * Restores the product's stock in `ProductService`.
   */
  remCartItem(cartItem: CartItem) {
    // Find the index of the item in the cart
    const cartItemIndex = this.cartItems.findIndex(item => cartItem.id == item.id);

    // If item is found, remove it from the cart
    if (cartItemIndex > -1) {
      this.cartItems.splice(cartItemIndex, 1);

      // Restore product's stock in ProductService
      this.productService.updateProductStock(cartItem.id, cartItem.unitsInStock);
      this.calcCartTotals();
    }
  }
}
