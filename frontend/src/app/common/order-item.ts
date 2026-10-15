import {CartItem} from "./cart-item";

// One item line of the checkout request, built from what's currently in the cart.
export class OrderItem {
  imageUrl:string // picture copied from the cart row
  unitPrice:number // price per unit copied from the cart row
  quantity:number // units the customer chose
  productId:string // which catalog product is being bought
  constructor(cartItem:CartItem) {
    this.imageUrl = cartItem.imageUrl
    this.unitPrice = cartItem.unitPrice
    this.quantity  = cartItem.quantity
    this.productId = cartItem.id
  }
}
