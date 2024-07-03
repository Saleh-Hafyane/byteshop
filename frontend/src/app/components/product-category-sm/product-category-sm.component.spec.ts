import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCategorySmComponent } from './product-category-sm.component';

describe('ProductCategorySmComponent', () => {
  let component: ProductCategorySmComponent;
  let fixture: ComponentFixture<ProductCategorySmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductCategorySmComponent]
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
