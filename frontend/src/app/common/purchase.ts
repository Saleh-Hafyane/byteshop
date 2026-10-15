import {Customer} from "./customer";
import {Address} from "./address";
import {Order} from "./order";
import {OrderItem} from "./order-item";

// Complete checkout payload posted to /api/checkout/purchase; exactly the three sections the backend expects.
export class Purchase {

  address:Address // where to deliver
  order:Order // cart totals
  orderItems:OrderItem[] // what is being bought
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
