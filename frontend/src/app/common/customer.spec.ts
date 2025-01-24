import { Customer } from './customer';

describe('Customer', () => {
  it('should create an instance', () => {
    const customer = new Customer('test-firstName', 'test-lastName', 'test-email');
    expect(customer).toBeTruthy();
  });
});
