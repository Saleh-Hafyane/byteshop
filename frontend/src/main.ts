// Angular entry point: starts AppComponent with the app-wide providers.
/// <reference types="@angular/localize" />

import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

// Any failure here means the app never rendered; the error lands in the console.
bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
