import { Component, OnInit, signal } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CurrencyPipe, NgForOf, NgIf } from '@angular/common';
import { FormService } from '../../services/form.service';
import { City } from '../../common/city';
import { CustomValidators } from '../../validators/customValidators';
import { CartService } from '../../services/cart.service';
import { Router } from '@angular/router';
import { CheckoutService } from '../../services/checkout.service';
import { Order } from '../../common/order';
import { OrderItem } from '../../common/order-item';
import { Purchase } from '../../common/purchase';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [ReactiveFormsModule, CurrencyPipe, NgForOf, NgIf],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css',
})
export class CheckoutComponent implements OnInit {
  checkoutFormGroup!: FormGroup;
  totalQuantity: number = 0;
  totalPrice: number = 0;
  creditCardMonths: number[] = [];
  creditCardYears: number[] = [];
  cities: City[] = [];
  constructor(
    private formService: FormService,
    private cartService: CartService,
    private router: Router,
    private checkoutService: CheckoutService
  ) {}
  ngOnInit() {
    this.checkoutFormGroup = new FormGroup({
      address: new FormGroup({
        city: new FormControl('', [Validators.required]),
        address: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          CustomValidators.notOnlySpaces,
        ]),
      }),
      creditCard: new FormGroup({
        cardType: new FormControl('', [Validators.required]),
        nameOnCard: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          CustomValidators.notOnlySpaces,
        ]),
        cardNumber: new FormControl('', [
          Validators.required,
          CustomValidators.notOnlySpaces,
          Validators.pattern('[0-9]{16}'),
        ]),
        securityCode: new FormControl('', [
          Validators.required,
          CustomValidators.notOnlySpaces,
          Validators.pattern('[0-9]{3}'),
        ]),
        expirationMonth: new FormControl(''),
        expirationYear: new FormControl(''),
      }),
    });

    // getting the months and years to populate the form

    const startMonth: number = new Date().getMonth() + 1;

    this.formService
      .getMonthsData(startMonth)
      .subscribe((data) => (this.creditCardMonths = data));
    this.formService
      .getYearsData()
      .subscribe((data) => (this.creditCardYears = data));
    // getting cities to populate the form
    this.formService.getCities().subscribe((data) => (this.cities = data));
    this.cardDetails();
  }

  get city() {
    return this.checkoutFormGroup.get('address.city');
  }
  get address() {
    return this.checkoutFormGroup.get('address.address');
  }
  get cardType() {
    return this.checkoutFormGroup.get('creditCard.cardType');
  }
  get nameOnCard() {
    return this.checkoutFormGroup.get('creditCard.nameOnCard');
  }
  get cardNumber() {
    return this.checkoutFormGroup.get('creditCard.cardNumber');
  }
  get securityCode() {
    return this.checkoutFormGroup.get('creditCard.securityCode');
  }

  onSubmit() {
    if (this.checkoutFormGroup.invalid) {
      this.checkoutFormGroup.markAllAsTouched();
      return;
    }

    // set up order
    let order = new Order(this.totalQuantity, this.totalPrice);

    // get cart items
    const cartItems = this.cartService.cartItems;

    // create orderItems from cartItems

    let orderItems: OrderItem[] = cartItems.map(
      (tempCartItem) => new OrderItem(tempCartItem)
    );

    // populate address
    let address = this.checkoutFormGroup.controls['address'].value;
    const city: City = JSON.parse(JSON.stringify(address.city));
    address.city = city.cityName;

    // set up purchase
    let purchase = new Purchase(address, order, orderItems);

    // call REST API via the CheckoutService
    this.checkoutService.makeOrder(purchase).subscribe({
      next: (response) => {
        alert(
          `Your order has been received.\nOrder tracking number: ${response.orderTrackingNumber}`
        );

        // reset cart
        this.resetCart();
      },
      error: (err) => {
        alert(`There was an error: ${err.message}`);
      },
    });
  }

  handleShowedMonths() {
    const currentYear: number = new Date().getFullYear();
    const selectedYear: number = Number(
      this.checkoutFormGroup
        .get('creditCard')!
        .get('expirationYear')!
        .getRawValue()
    );
    let startMonth: number;
    if (selectedYear === currentYear) {
      startMonth = new Date().getMonth() + 1;
    } else {
      startMonth = 1;
    }
    this.formService
      .getMonthsData(startMonth)
      .subscribe((data) => (this.creditCardMonths = data));
  }

  private cardDetails() {
    this.cartService.totalQuantity.subscribe(
      (data) => (this.totalQuantity = data)
    );
    this.cartService.totalPrice.subscribe((data) => (this.totalPrice = data));
  }

  private resetCart() {
    //reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalQuantity.next(0);
    this.cartService.totalPrice.next(0);
    //reset form data
    this.checkoutFormGroup.reset();
    // navigate to product page
    this.router.navigateByUrl('/products');
  }

  protected readonly console = console;
}
