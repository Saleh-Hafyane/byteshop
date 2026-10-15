// Route guard: only ROLE_ADMIN may enter the admin area.
import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';

// Admins pass; logged-in regular users go to the catalog; guests go to login.
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Role comes from the JWT claim, checked client-side (backend still enforces it too).
  if (authService.getRole() === 'ADMIN') {
    return true;
  }
  // Logged in but not admin: silently redirect away.
  if (authService.isAuthenticated()) {
    router.navigate(['/products']);
    return false;
  }
  // Not logged in at all: send to the login page.
  router.navigate(['/login']);
  return false;
};
