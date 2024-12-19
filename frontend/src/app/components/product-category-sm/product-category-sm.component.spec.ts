import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCategorySmComponent } from './product-category-sm.component';
import {HttpClientModule} from "@angular/common/http";
import {provideRouter} from "@angular/router";
import {routes} from "../../app.routes";

describe('ProductCategorySmComponent', () => {
  let component: ProductCategorySmComponent;
  let fixture: ComponentFixture<ProductCategorySmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCategorySmComponent,HttpClientModule],
      providers:[provideRouter(routes)]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCategorySmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
