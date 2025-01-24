import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpRequest } from '@angular/common/http';
import { JwtInterceptorService } from './jwt-interceptor.service';
import {CartService} from "./cart.service";

describe('JwtInterceptorService', () => {
  let service:JwtInterceptorService
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        JwtInterceptorService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true },
      ],
    });
    service = TestBed.inject(JwtInterceptorService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);

    // Clear localStorage before each test
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding requests
  });
  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('should add Authorization header if a valid token is present in localStorage', () => {
    // Arrange: Set a valid token in localStorage
    const mockToken = 'mock-valid-jwt-token';
    localStorage.setItem('token', mockToken);

    // Act: Make an HTTP GET request
    httpClient.get('/api/test').subscribe();

    // Assert: Verify the Authorization header is added with the correct token
    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBeTrue();
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
  });

  it('should not add Authorization header if token is not present in localStorage', () => {
    // Act: Make an HTTP GET request without setting a token in localStorage
    httpClient.get('/api/test').subscribe();

    // Assert: Verify the Authorization header is not added
    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBeFalse();
  });

});
