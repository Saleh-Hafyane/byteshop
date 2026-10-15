import {Product} from "./product";

// One line in the shopping cart: product info copied at add time, starting at quantity 1.
export class CartItem {
  id:string // product id, also the key used to find it again in the cart
  name:string // product title shown in the cart table
  imageUrl:string // picture for the cart row
  unitPrice:number // price per unit at the moment it was added
  quantity:number // units in the cart, changed by +/- buttons
  unitsInStock:number // max available, checked before increasing quantity
  constructor(product:Product) {
    this.id = product.id
    this.name = product.name
    this.imageUrl=product.imageUrl
    this.unitPrice=product.unitPrice
    this.quantity  =  1 // always starts with a single unit
    this.unitsInStock = product.unitsInStock
  }
}
