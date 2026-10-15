// Cart totals sent as the "order" section of the checkout request.
export class Order {
  totalQuantity:number // sum of all item quantities
  totalPrice:number // grand total the customer pays
  constructor(totalQuantity:number,
  totalPrice:number) {
    this.totalQuantity = totalQuantity
    this.totalPrice = totalPrice

  }


}
