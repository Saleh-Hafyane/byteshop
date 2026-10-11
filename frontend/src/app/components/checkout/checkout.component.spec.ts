import { TestBed, ComponentFixture } from '@angular/core/testing';
import { BehaviorSubject, of } from 'rxjs';

import { CheckoutComponent } from './checkout.component';
import { FormService } from '../../services/form.service';
import { CartService } from '../../services/cart.service';
import { CheckoutService } from '../../services/checkout.service';
import { Router } from '@angular/router';
import { CartItem } from '../../common/cart-item';
import { Purchase } from '../../common/purchase';

/**
 * Regression guards for the checkout payload construction.
 * The bug this locks in: the address sent to the API must use the exact
 * AddressDTO keys ({ city, fullAddress }) with city as the selected
 * city-name string - not a City object or a different key name.
 */
describe('CheckoutComponent', () => {
  let component: CheckoutComponent;
  let fixture: ComponentFixture<CheckoutComponent>;
  let checkoutSpy: jasmine.SpyObj<CheckoutService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const seedCartItems: CartItem[] = [
    {
      id: '1',
      name: 'Smartphone X1',
      imageUrl: 'img-1.jpg',
      unitPrice: 4000,
      quantity: 1,
      unitsInStock: 10,
    } as CartItem,
    {
      id: '2',
      name: 'Wireless Headphones',
      imageUrl: 'img-2.jpg',
      unitPrice: 4000,
      quantity: 1,
      unitsInStock: 10,
    } as CartItem,
  ];

  const cartServiceStub: any = {
    cartItems: [] as CartItem[],
    totalQuantity: new BehaviorSubject<number>(0),
    totalPrice: new BehaviorSubject<number>(0),
  };

  const formServiceStub = {
    getCities: () => of([]),
    getMonthsData: () => of([1, 2, 3]),
    getYearsData: () => of([2026, 2027]),
  };

  beforeEach(async () => {
    checkoutSpy = jasmine.createSpyObj('CheckoutService', ['makeOrder']);
    checkoutSpy.makeOrder.and.returnValue(
      of({ orderTrackingNumber: 'TRACK-123' })
    );
    routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl']);

    // fresh cart state for every test
    cartServiceStub.cartItems = [...seedCartItems];
    cartServiceStub.totalQuantity = new BehaviorSubject<number>(2);
    cartServiceStub.totalPrice = new BehaviorSubject<number>(8000);

    await TestBed.configureTestingModule({
      imports: [CheckoutComponent],
      providers: [
        { provide: FormService, useValue: formServiceStub },
        { provide: CartService, useValue: cartServiceStub },
        { provide: CheckoutService, useValue: checkoutSpy },
        { provide: Router, useValue: routerSpy },
      ],
    })
      // class-logic tests only; the template is display wiring
      .overrideTemplate(CheckoutComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CheckoutComponent);
    component = fixture.componentInstance;
    spyOn(window, 'alert');
    fixture.detectChanges(); // triggers ngOnInit
  });

  function fillValidForm() {
    component.checkoutFormGroup.get('address.city')!.setValue('Rabat');
    component.checkoutFormGroup
      .get('address.fullAddress')!
      .setValue('12 Avenue Mohammed V');
    component.checkoutFormGroup.get('creditCard.cardType')!.setValue('visa');
    component.checkoutFormGroup
      .get('creditCard.nameOnCard')!
      .setValue('Test User');
    component.checkoutFormGroup
      .get('creditCard.cardNumber')!
      .setValue('4111111111111111');
    component.checkoutFormGroup
      .get('creditCard.securityCode')!
      .setValue('123');
  }

  it('submits a payload with the exact AddressDTO keys, cart totals and mapped order items', () => {
    fillValidForm();

    component.onSubmit();

    expect(checkoutSpy.makeOrder).toHaveBeenCalledTimes(1);
    const purchase = checkoutSpy.makeOrder.calls.mostRecent()
      .args[0] as Purchase;

    // address shape: exactly { city, fullAddress }, city is the city-name string
    expect(Object.keys(purchase.address).sort()).toEqual([
      'city',
      'fullAddress',
    ]);
    expect(purchase.address.city).toBe('Rabat');
    expect(purchase.address.fullAddress).toBe('12 Avenue Mohammed V');

    // totals come from the cart state captured on init
    expect(purchase.order.totalQuantity).toBe(2);
    expect(purchase.order.totalPrice).toBe(8000);

    // cart items are mapped to order items with the product id as string
    expect(purchase.orderItems.length).toBe(2);
    expect(purchase.orderItems[0]).toEqual(
      jasmine.objectContaining({
        productId: '1',
        quantity: 1,
        unitPrice: 4000,
        imageUrl: 'img-1.jpg',
      })
    );

    // success path: cart emptied and the user is sent back to the shop
    expect(cartServiceStub.cartItems).toEqual([]);
    expect(routerSpy.navigateByUrl).toHaveBeenCalledWith('/products');
  });

  it('does not call the API when the form is invalid', () => {
    // address and credit-card controls left empty

    component.onSubmit();

    expect(checkoutSpy.makeOrder).not.toHaveBeenCalled();
    expect(component.checkoutFormGroup.invalid).toBeTrue();
  });
});
