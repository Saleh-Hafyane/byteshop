import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartDetailComponent } from './cart-detail.component';
import {HttpClientModule} from "@angular/common/http";

describe('CartDetailComponent', () => {
  let component: CartDetailComponent;
  let fixture: ComponentFixture<CartDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartDetailComponent,HttpClientModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CartDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
