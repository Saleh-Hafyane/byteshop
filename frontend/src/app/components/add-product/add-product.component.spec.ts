// Unit tests for AddProductComponent's image upload flow: validation, upload-then-save, removal.
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AddProductComponent } from './add-product.component';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';

describe('AddProductComponent (image upload)', () => {
  let component: AddProductComponent;
  let productServiceSpy: jasmine.SpyObj<ProductService>;
  let categoryServiceSpy: jasmine.SpyObj<CategoryService>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockCategories = [{ id: 1, categoryName: 'Laptops' }];

  beforeEach(async () => {
    productServiceSpy = jasmine.createSpyObj('ProductService', ['addProduct', 'updateProduct', 'getProduct', 'uploadImage']);
    categoryServiceSpy = jasmine.createSpyObj('CategoryService', ['getProductCategories', 'getProductCategory']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    categoryServiceSpy.getProductCategories.and.returnValue(of(mockCategories as any));

    await TestBed.configureTestingModule({
      imports: [AddProductComponent],
      providers: [
        { provide: ProductService, useValue: productServiceSpy },
        { provide: CategoryService, useValue: categoryServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: HttpClient, useValue: {} },
        {
          provide: ActivatedRoute,
          useValue: { paramMap: of(convertToParamMap({})), snapshot: { paramMap: convertToParamMap({}) } }
        }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(AddProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  function fillValidForm() {
    component.addProductFormGroup.patchValue({
      category: 'Laptops',
      name: 'Test Phone',
      description: 'Nice phone',
      unitPrice: '99.99',
      unitsInStock: '5'
    });
    component.categories = mockCategories as any;
  }

  it('should reject non-image files', () => {
    const file = new File(['hello'], 'notes.txt', { type: 'text/plain' });
    const input = document.createElement('input');
    Object.defineProperty(input, 'files', { value: [file] });
    component.onFileSelected({ target: input } as any);
    expect(component.selectedFile).toBeNull();
    expect(component.imageError).toContain('Only JPEG');
  });

  it('should preview a valid image and upload-then-save on submit', () => {
    fillValidForm();
    const file = new File(['img'], 'photo.png', { type: 'image/png' });
    const input = document.createElement('input');
    Object.defineProperty(input, 'files', { value: [file] });
    component.onFileSelected({ target: input } as any);
    expect(component.selectedFile).toBe(file);
    expect(component.previewUrl).toContain('blob:');

    productServiceSpy.uploadImage.and.returnValue(of({ url: '/api/files/abc.png' }));
    productServiceSpy.addProduct.and.returnValue(of({}));
    spyOn(window, 'alert');

    component.onSubmit();

    expect(productServiceSpy.uploadImage).toHaveBeenCalledWith(file);
    expect(productServiceSpy.addProduct).toHaveBeenCalledWith(jasmine.objectContaining({ imageUrl: '/api/files/abc.png' }));
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/products']);
  });

  it('should allow create without image (sends empty url)', () => {
    fillValidForm();
    productServiceSpy.addProduct.and.returnValue(of({}));
    spyOn(window, 'alert');

    component.onSubmit();

    expect(productServiceSpy.uploadImage).not.toHaveBeenCalled();
    expect(productServiceSpy.addProduct).toHaveBeenCalledWith(jasmine.objectContaining({ imageUrl: '' }));
  });

  it('should clear the existing image on remove', () => {
    component.existingImageUrl = '/api/files/old.png';
    component.removeImage();
    expect(component.existingImageUrl).toBeNull();
    expect(component.effectiveImage).toBeNull();

    fillValidForm();
    productServiceSpy.updateProduct.and.returnValue(of({}));
    component.isToUpdate = true;
    component.productIdToUpdate = 7;
    spyOn(window, 'alert');

    component.onSubmit();

    expect(productServiceSpy.uploadImage).not.toHaveBeenCalled();
    expect(productServiceSpy.updateProduct).toHaveBeenCalledWith(7, jasmine.objectContaining({ imageUrl: '' }));
  });
});
