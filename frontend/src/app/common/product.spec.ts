import { Product } from './product';

describe('Product', () => {
  it('should create an instance', () => {
    const product = new Product('1',
      'T-P',
      'Test Product',
      'Test Description',
      4000,
      'test-image.jpg',
      true,
      10,
      new Date(),
      new Date())
    expect(product).toBeTruthy();
  });
});
