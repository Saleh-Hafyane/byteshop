// App-wide provider setup: router, HTTP client, forms, and the JWT interceptor.
import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {ProductService} from "./services/product.service";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { JwtInterceptorService } from "./services/jwt-interceptor.service";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {FormsModule} from "@angular/forms";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

// Passed to bootstrapApplication in main.ts.
export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes), // the route table from app.routes.ts
    ProductService, // manually provided so tests can swap it out
    importProvidersFrom(HttpClientModule), // enables HttpClient everywhere
    importProvidersFrom(NgbModule), // ng-bootstrap widgets (datepicker, tooltip...)
    importProvidersFrom(FormsModule), // template-driven forms
    provideAnimationsAsync(), // Angular Material-style animations without blocking startup
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true } // every request runs through the JWT interceptor
  ]
};
