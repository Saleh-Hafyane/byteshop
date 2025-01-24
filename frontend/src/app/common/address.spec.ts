import { Address } from './address';

describe('Address', () => {
  it('should create an instance', () => {
    const address = new Address("Rabat","Avenue")
    expect(address).toBeTruthy();
  });
});
