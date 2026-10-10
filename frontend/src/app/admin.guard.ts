// admin.guard.ts
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.getRole() === 'ADMIN') {
    return true;
  }
  if (authService.isAuthenticated()) {
    router.navigate(['/products']);
    return false;
  }
  router.navigate(['/login']);
  return false;
};
