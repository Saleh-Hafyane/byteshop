// Attaches the JWT from localStorage to every outgoing HTTP request automatically.
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

// Registered as a provider in app.config.ts so HttpClient always runs through it.
@Injectable()
export class JwtInterceptorService implements HttpInterceptor {

  // Clones the request with an Authorization header when a usable token exists.
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');
    // Guards against empty, "undefined" or "null" junk values stored by older sessions.
    if (token && token.trim() !== '' && token !== 'undefined' && token !== 'null') {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(request);
  }
}
