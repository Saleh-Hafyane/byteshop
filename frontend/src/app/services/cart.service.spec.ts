import { TestBed } from '@angular/core/testing';
import { CartService } from './cart.service';
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ProductService } from "./product.service";
import { CartItem } from "../common/cart-item";
import { BehaviorSubject } from "rxjs";

describe('CartService', () => {
  let service: CartService;
  let productServiceSpy: jasmine.SpyObj<ProductService>;

  // Mock data
  const mockCartItem: CartItem = {
    id: '1',
    name: 'Test Product',
    imageUrl: 'https://example.com/sample.jpg',
    unitPrice: 1000,
    quantity: 1,
    unitsInStock: 10
  };

  beforeEach(() => {
    // Create a spy object for ProductService
    const spy = jasmine.createSpyObj('ProductService', [
      'getProductStock',
      'updateProductStock'
    ]);

    // Setup TestBed
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        CartService,
        { provide: ProductService, useValue: spy }
      ],
    });

    service = TestBed.inject(CartService);
    productServiceSpy = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    service.cartItems = []

  });

  afterEach(() =>{
    service.cartItems = []
    productServiceSpy.getProductStock.calls.reset();
    productServiceSpy.updateProductStock.calls.reset();
  })

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add an existing item to the cart and update totals', () => {
    // Mock ProductService behavior
    productServiceSpy.getProductStock.and.returnValue(10);

    // Add the item to the cart twice
    service.addToCart(mockCartItem);
    const result = service.addToCart(mockCartItem);

    // Verify item was added successfully
    expect(result).toBeTrue();
    expect(service.cartItems.length).toBe(1);
    expect(service.cartItems[0].quantity).toBe(2);

    // Verify totals are updated
    service.totalQuantity.subscribe((quantity) => {
      expect(quantity).toBe(2);
    });

    service.totalPrice.subscribe((price) => {
      expect(price).toBe(2000);
    });

  });
  it('should remove an item and update totals', () => {
    // Mock ProductService behavior
    productServiceSpy.getProductStock.and.returnValue(10);

    // Add item to the cart
    service.addToCart(mockCartItem);

    // Remove the item
    service.remCartItem(mockCartItem);

    // Verify the cart is empty
    expect(service.cartItems.length).toBe(0);

    // Verify totals are updated
    service.totalQuantity.subscribe((quantity) => {
      expect(quantity).toBe(0);

    });

    service.totalPrice.subscribe((price) => {
      expect(price).toBe(0);

    });

  });
  it('should handle cases where stock is unavailable', () => {
    // Mock ProductService behavior for no stock
    productServiceSpy.getProductStock.and.returnValue(0);

    // Attempt to add an item with no stock
    const result = service.addToCart(mockCartItem);

    // Verify item was not added
    expect(result).toBeFalse();
    expect(service.cartItems.length).toBe(0);

  });
  // todo: fix decrease test case
  /*
    it('should decrease an item quantity and update totals', () => {
      // Mock ProductService behavior
      productServiceSpy.getProductStock.and.returnValue(10);

      // Add item to the cart twice
      service.addToCart(mockCartItem);
      service.addToCart(mockCartItem);

      // Decrease item quantity
      service.decQuantity(mockCartItem);

      // Verify quantity and totals
      expect(service.cartItems[0].quantity).toBe(1);

      service.totalQuantity.subscribe((quantity) => {
        expect(quantity).toBe(1);

      });
      service.totalPrice.subscribe((price) => {
        expect(price).toBe(1000);

      });

    });
  */
  // todo: fix add new item test case
  /*
  it('should add a new item to the cart and update totals', () => {
    // Mock ProductService behavior
    productServiceSpy.getProductStock.and.returnValue(undefined);

    // Add the item to the cart
    service.addToCart(mockCartItem);

    // Verify item was added successfully
    expect(service.cartItems.length).toBe(1);
    expect(service.cartItems[0]).toEqual(mockCartItem);

    // Verify totals are updated
    service.totalQuantity.subscribe((quantity) => {
      expect(quantity).toBe(1);

    });

    service.totalPrice.subscribe((price) => {
      expect(price).toBe(1000);

    });

    // Verify stock update in ProductService
    expect(productServiceSpy.updateProductStock).toHaveBeenCalledWith('1', 9);


  });
*/
});
