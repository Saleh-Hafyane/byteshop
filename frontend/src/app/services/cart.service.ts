import { Injectable } from '@angular/core';
import {CartItem} from "../common/cart-item";
import {BehaviorSubject, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartItems:CartItem[] = []
  totalQuantity:Subject<number> = new BehaviorSubject<number>(0)
  totalPrice:Subject<number> = new BehaviorSubject<number>(0)
  constructor() { }
  addToCart(item:CartItem){
    let existInCartItems:boolean = false
    let existingCartItem:CartItem|undefined = undefined
    if (this.cartItems.length>0){
      existingCartItem = this.cartItems.find(cartItem=>cartItem.id ===item.id)
      existInCartItems = (existingCartItem!=undefined)


    }
    if (existInCartItems){

      existingCartItem!.quantity++
    }
    else {
      this.cartItems.push(item)
    }
    this.calcCartTotals()

  }

  calcCartTotals() {
    let quantityTotalValue:number = 0
    let priceTotalValue:number = 0.00
    for (let cartItem of this.cartItems){
      quantityTotalValue += cartItem.quantity
      priceTotalValue += cartItem.quantity * cartItem.unitPrice
    }
    this.totalQuantity.next(quantityTotalValue)
    this.totalPrice.next(priceTotalValue)

  }





  decQuantity(cartItem: CartItem) {
    cartItem.quantity--
    if (cartItem.quantity==0){
      this.remCartItem(cartItem)
    }
    else {
      this.calcCartTotals()
    }
  }
  remCartItem(cartItem: CartItem) {
    const cartItemIndex  = this.cartItems.findIndex(item => cartItem.id == item.id)
    if (cartItemIndex>-1){
      this.cartItems.splice(cartItemIndex,1)
      this.calcCartTotals()
    }

  }
}
