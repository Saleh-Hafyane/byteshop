import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartStatusComponent } from './cart-status.component';
import {HttpClientModule} from "@angular/common/http";
import {provideRouter} from "@angular/router";
import {routes} from "../../app.routes";

describe('CartStatusComponent', () => {
  let component: CartStatusComponent;
  let fixture: ComponentFixture<CartStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartStatusComponent,HttpClientModule],
      providers:[provideRouter(routes)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CartStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
