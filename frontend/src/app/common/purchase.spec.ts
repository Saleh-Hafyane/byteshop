import { Purchase } from './purchase';
import { Address } from './address';
import { Order } from './order';
import { OrderItem } from './order-item';

describe('Purchase', () => {
  it('should create an instance', () => {
    // Mock Address object
    const mockAddress: Address = {
      city: 'Rabat',
      fullAddress: 'Avenue'
    };

    // Mock Order object
    const mockOrder: Order = {
      totalQuantity: 2,
      totalPrice: 8000
    };

    // Mock OrderItem objects
    const mockOrderItems: OrderItem[] = [
      {
        imageUrl: 'test-image1.jpg',
        unitPrice: 4000,
        quantity: 1,
        productId: '1'
      },
      {
        imageUrl: 'test-image2.jpg',
        unitPrice: 4000,
        quantity: 1,
        productId: '2'
      }
    ];

    const purchase = new Purchase(mockAddress, mockOrder, mockOrderItems);

    // Assert that the instance is created successfully
    expect(purchase).toBeTruthy();

    // Additional checks to ensure properties are assigned correctly
    expect(purchase.address).toBe(mockAddress);
    expect(purchase.order).toBe(mockOrder);
    expect(purchase.orderItems).toBe(mockOrderItems);
  });
});
