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

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  describe('register', () => {
    it('should send a POST request to register a new user', () => {
      const mockRequest = { username: 'testUser', password: 'password' };
      const mockResponse: User = { username: 'testUser', token: 'token' };

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
      const mockResponse: User = { username: 'testUser', token: 'token' }; // Mock response

      service.login({ getRawValue: () => mockRequest }).subscribe((response) => {
        expect(response).toEqual(mockResponse);
      });

      const req = mockHttp.expectOne(`${service['apiUrl']}/authenticate`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRequest);
      req.flush(mockResponse);
    });
  });
  describe('logout', () => {
    it('should clear localStorage and navigate to login', () => {
      spyOn(localStorage, 'removeItem'); // Spy on localStorage.removeItem to verify it's called

      service.logout(); // Call the logout method

      expect(localStorage.removeItem).toHaveBeenCalledWith('token'); // Check if the token was removed
      expect(localStorage.removeItem).toHaveBeenCalledWith('username'); // Check if the username was removed
      expect(service.userSig()).toBeNull(); // Verify the user signal is reset
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']); // Verify navigation to the login page
    });
  });
  describe('isAuthenticated', () => {
    it('should return true if token exists in localStorage', () => {
      spyOn(localStorage, 'getItem').and.returnValue('token'); // Mock localStorage to return a token
      expect(service.isAuthenticated()).toBeTrue(); // Expect isAuthenticated to return true
    });

    it('should return false if no token exists in localStorage', () => {
      spyOn(localStorage, 'getItem').and.returnValue(null); // Mock localStorage to return null
      expect(service.isAuthenticated()).toBeFalse(); // Expect isAuthenticated to return false
    });
  });
  describe('token and username storage', () => {
    it('should save and retrieve the token correctly', () => {
      spyOn(localStorage, 'setItem');
      spyOn(localStorage, 'getItem').and.returnValue('token'); // Mock localStorage to return a token

      service.saveToken('token');
      expect(localStorage.setItem).toHaveBeenCalledWith('token', 'token'); // Verify it was stored
      expect(service.getToken()).toBe('token'); // Verify it can be retrieved
    });

    it('should save and retrieve the username correctly', () => {
      spyOn(localStorage, 'setItem');
      spyOn(localStorage, 'getItem').and.returnValue('testUser'); // Mock localStorage to return a username

      service.saveUsername('testUser'); // Save a username
      expect(localStorage.setItem).toHaveBeenCalledWith('username', 'testUser'); // Verify it was stored
      expect(service.getUsername()).toBe('testUser'); // Verify it can be retrieved
    });
  });
  describe('getAuthHeaders', () => {
    it('should return HTTP headers with the Authorization token', () => {
      spyOn(localStorage, 'getItem').and.returnValue('token');
      const headers = service.getAuthHeaders();
      expect(headers.get('Authorization')).toBe('Bearer token'); // Check the Authorization header
    });
  });
});
