import { CartItem } from './cart-item';
import {Product} from "./product";

describe('CartItem', () => {
  it('should create an instance', () => {
    const mockProduct:Product = {
      id: '1',
      sku: 'T-P',
      name: 'Test Product',
      description: 'Test Description',
      imageUrl: 'test-image.jpg',
      active:true,
      unitPrice: 4000,
      unitsInStock: 10,
      dateCreated: new Date(),
      lastUpdated: new Date(),
    }
    const cartItem = new CartItem(mockProduct)
    expect(cartItem).toBeTruthy();
  });
});
