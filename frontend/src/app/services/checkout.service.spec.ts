import { TestBed } from '@angular/core/testing';

import { CheckoutService } from './checkout.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Purchase} from "../common/purchase";

describe('CheckoutService', () => {
  let service: CheckoutService;
  let mockHttp: HttpTestingController

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Add this module
      providers: [CheckoutService]
    });
    service = TestBed.inject(CheckoutService);
    mockHttp = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it("should submit a purchase order", () => {
    const mockPurchase: Purchase = {
      address: {
        city: 'Rabat',
        fullAddress: 'Avenue'
      },
      order: {
        totalQuantity: 2,
        totalPrice: 8000
      },
      orderItems: [
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
        }]
    }
    service.makeOrder( mockPurchase).subscribe(response => {
      expect(response).toBeTruthy()
      expect(response).toEqual(mockPurchase);
    })

    const req = mockHttp.expectOne(`${service['purchaseUrl']}`);
    expect(req.request.method).toBe('POST'); // Ensure the request is a POST
    expect(req.request.body).toEqual(mockPurchase); // Ensure the request body matches the mock data
    req.flush(mockPurchase);
  });
})
