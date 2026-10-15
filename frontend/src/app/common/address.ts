// Delivery address entered at checkout, matching the backend AddressDTO.
export class Address {
  city:string // city name, e.g. "Rabat"
  fullAddress:string // street line for the courier
  constructor(city:string,fullAddress:string) {
    this.city = city
    this.fullAddress = fullAddress
  }
}
