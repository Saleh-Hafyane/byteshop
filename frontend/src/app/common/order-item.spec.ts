import { OrderItem } from './order-item';
import {CartItem} from "./cart-item";

describe('OrderItem', () => {
  it('should create an instance', () => {
    const mockCartItem: CartItem = {
      id: '1',
      name: 'Test Product',
      imageUrl: 'test-image.jpg',
      unitPrice: 4000,
      quantity: 10,
      unitsInStock: 10
    };
    expect(new OrderItem(mockCartItem)).toBeTruthy();
  });
});
