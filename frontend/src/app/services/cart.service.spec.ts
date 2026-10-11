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
  it('should round the cart total to exact cents (no floating-point artifacts)', () => {
    // Raw float sum of these prices is 249.96999999999997;
    // the emitted total must be the clean 249.97 (regression: "249.97000000000003" in UI)
    productServiceSpy.getProductStock.and.returnValue(10);

    service.addToCart({ id: 'a', name: 'Item A', imageUrl: '', unitPrice: 83.32, quantity: 1, unitsInStock: 10 });
    service.addToCart({ id: 'b', name: 'Item B', imageUrl: '', unitPrice: 83.33, quantity: 1, unitsInStock: 10 });
    service.addToCart({ id: 'c', name: 'Item C', imageUrl: '', unitPrice: 83.32, quantity: 1, unitsInStock: 10 });

    let price!: number;
    service.totalPrice.subscribe((p) => (price = p));

    expect(price).toBe(249.97);
  });

  it('should decrease an item quantity and update totals', () => {
    productServiceSpy.getProductStock.and.returnValue(10);
    // fresh copy with pinned quantity: addToCart/decQuantity mutate the item
    // object, and the shared mockCartItem is mutated by other tests
    const item: CartItem = { ...mockCartItem, quantity: 1 };

    service.addToCart(item);
    service.addToCart(item); // same reference already in cart -> quantity 2

    service.decQuantity(item);

    expect(item.quantity).toBe(1);
    expect(service.cartItems.length).toBe(1);

    let quantity!: number;
    let price!: number;
    service.totalQuantity.subscribe((q) => (quantity = q));
    service.totalPrice.subscribe((p) => (price = p));
    expect(quantity).toBe(1);
    expect(price).toBe(1000);
  });

  it('should add a brand-new item when the product has no stock entry yet', () => {
    // no stock record yet: ProductService reports undefined for this product id
    productServiceSpy.getProductStock.and.returnValue(undefined);
    const item: CartItem = { ...mockCartItem, quantity: 1 };

    service.addToCart(item);

    expect(service.cartItems.length).toBe(1);
    expect(service.cartItems[0]).toBe(item);

    let quantity!: number;
    let price!: number;
    service.totalQuantity.subscribe((q) => (quantity = q));
    service.totalPrice.subscribe((p) => (price = p));
    expect(quantity).toBe(1);
    expect(price).toBe(1000);

    // the product's stock is seeded from the item's unitsInStock
    expect(productServiceSpy.updateProductStock).toHaveBeenCalledWith('1', 9);
  });
});