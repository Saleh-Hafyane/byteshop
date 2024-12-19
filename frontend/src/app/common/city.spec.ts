import { City } from './city';

describe('City', () => {
  it('should create an instance', () => {
    const city = new City(1,"Rabat")
    expect(city).toBeTruthy();
  });
});
