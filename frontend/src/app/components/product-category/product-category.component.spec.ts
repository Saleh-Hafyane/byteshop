import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCategoryComponent } from './product-category.component';
import {HttpClientModule} from "@angular/common/http";

describe('ProductCategoryComponent', () => {
  let component: ProductCategoryComponent;
  let fixture: ComponentFixture<ProductCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCategoryComponent,HttpClientModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
