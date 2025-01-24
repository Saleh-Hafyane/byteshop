import {Customer} from "./customer";
import {Address} from "./address";
import {Order} from "./order";
import {OrderItem} from "./order-item";

export class Purchase {

  address:Address
  order:Order
  orderItems:OrderItem[]
  constructor(

     address:Address,
     order:Order,
     orderItems:OrderItem[]
  ) {

    this.address = address
    this.order = order
    this.orderItems = orderItems
  }
}
