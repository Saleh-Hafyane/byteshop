import { TestBed } from '@angular/core/testing';
import { CanActivateFn, Router } from '@angular/router';

import { authGuard } from './auth.guard';
import { AuthService } from './services/auth.service';

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(() => {
    authService = jasmine.createSpyObj('AuthService', ['isAuthenticated']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
    });
  });

  it('allows navigation when the user is authenticated', () => {
    authService.isAuthenticated.and.returnValue(true);

    expect(executeGuard({} as any, {} as any)).toBeTrue();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('blocks navigation and redirects to /login when not authenticated', () => {
    authService.isAuthenticated.and.returnValue(false);

    expect(executeGuard({} as any, {} as any)).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});