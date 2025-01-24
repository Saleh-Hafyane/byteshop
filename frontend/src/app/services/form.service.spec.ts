import { TestBed } from '@angular/core/testing';

import { FormService } from './form.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {City} from "../common/city";

describe('FormService', () => {
  let service: FormService;
  let httpMock: HttpTestingController;


  beforeEach(() => {
    TestBed.configureTestingModule({imports: [HttpClientTestingModule], // Use HttpClientTestingModule for mocking HTTP requests
      providers: [FormService],});
    service = TestBed.inject(FormService);
    httpMock = TestBed.inject(HttpTestingController);

  });
  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding requests
  });
  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  describe('getCities', () => {
    it('should fetch cities from the API', () => {
      const mockCities: City[] = [
        { id: 1, cityName: 'City A' },
        { id: 2, cityName: 'City B' },
      ];

      // Call the method
      service.getCities().subscribe((cities) => {
        expect(cities.length).toBe(2);
        expect(cities).toEqual(mockCities);
      });

      // Verify the request and send mock data
      const req = httpMock.expectOne('http://localhost:8080/api/city');
      expect(req.request.method).toBe('GET');
      req.flush({ _embedded: { city: mockCities } });
    });
  });

  describe('getMonthsData', () => {
    it('should return months starting from the specified month', () => {
      const startMonth = 7;
      const expectedMonths = [7, 8, 9, 10, 11, 12];

      service.getMonthsData(startMonth).subscribe((months) => {
        expect(months).toEqual(expectedMonths);
      });
    });
  });

  describe('getYearsData', () => {
    it('should return the next 10 years starting from the current year', () => {
      const currentYear = new Date().getFullYear();
      const expectedYears = Array.from({ length: 11 }, (_, i) => currentYear + i);

      service.getYearsData().subscribe((years) => {
        expect(years).toEqual(expectedYears);
      });
    });
  });
});
