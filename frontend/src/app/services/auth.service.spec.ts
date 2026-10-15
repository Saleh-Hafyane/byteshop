// Unit tests for AuthService: register and login POST contracts only.
import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Router} from "@angular/router";
import {User} from "../common/user";

describe('AuthService', () => {
  let service: AuthService;
  let mockHttp: HttpTestingController;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(() => {
    // Create a spy object for Router
    mockRouter = jasmine.createSpyObj("Router",['navigate']);
    TestBed.configureTestingModule({imports: [HttpClientTestingModule], // Import HttpClientTestingModule to mock HTTP calls
      providers: [AuthService,{provide: Router, useValue: mockRouter}]}); // Provide the mock Router instead of the real one
    service = TestBed.inject(AuthService);
    mockHttp = TestBed.inject(HttpTestingController);
  });
  // Ensure no unmatched HTTP requests remain after each test
  afterEach(()=>{
    mockHttp.verify()
  })

  describe('register', () => {
    it('should send a POST request to register a new user', () => {
      const mockRequest = { username: 'testUser', password: 'password' };
      const mockResponse: User = { username: 'testUser', token: 'token', role: 'USER' };

      // Call the register method and assert the response
      service.register({ getRawValue: () => mockRequest }).subscribe((response) => {
        expect(response).toEqual(mockResponse); // Check if the response matches the mock response
      });

      // Expect a POST request to the correct URL
      const req = mockHttp.expectOne(`${service['apiUrl']}/register`);
      expect(req.request.method).toBe('POST'); // Ensure the request is a POST
      expect(req.request.body).toEqual(mockRequest); // Ensure the request body matches the mock data
      req.flush(mockResponse); // Simulate the server response
    });
  });
  describe('login', () => {
    it('should send a POST request to authenticate a user', () => {
      const mockRequest = { username: 'testUser', password: 'password' }; // Mock login payload
      const mockResponse: User = { username: 'testUser', token: 'token', role: 'USER' }; // Mock response

      service.login({ getRawValue: () => mockRequest }).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = mockHttp.expectOne(`${service['apiUrl']}/authenticate`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRequest);
      req.flush(mockResponse);
    });
  });
});
