// Buyer's name and email as asked for on the checkout form.
export class Customer {
  firstName:string // given name of the buyer
  lastName:string // family name of the buyer
  email:string // contact address for order updates
  constructor( firstName:string,
  lastName:string, email:string) {

    this.firstName = firstName
    this.lastName = lastName
    this.email = email
  }
}
