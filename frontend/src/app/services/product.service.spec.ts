import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { Product } from '../common/product';
import { ProductCategory } from '../common/product-category';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;

  const mockProduct: Product = {
    id: "1",
    sku: 'T-P',
    name: 'Test Product',
    description: 'Sample product description',
    unitPrice: 1000,
    imageUrl: 'https://example.com/sample.jpg',
    active: true,
    unitsInStock: 10,
    dateCreated: new Date(),
    lastUpdated: new Date()
  };

  const mockCategory: ProductCategory = {
    id: 1,
    categoryName: 'Category 1'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch paginated products by category', () => {
    const mockResponse = {
      _embedded: { products: [mockProduct] },
      page: {
        size: 10,
        totalElements: 1,
        totalPages: 1,
        number: 0
      }
    };

    service.getProductsPagination(0, 10, 1, 'name').subscribe(response => {
      expect(response._embedded.products.length).toBe(1);
      expect(response._embedded.products[0]).toEqual(mockProduct);
    });

    const req = httpMock.expectOne(
      'http://localhost:8080/api/products/search/findByCategoryId?id=1&page=0&size=10&sort=name'
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should fetch product categories', () => {
    const mockResponse = {
      _embedded: { productCategory: [mockCategory] }
    };

    service.getProductCategories().subscribe(categories => {
      expect(categories.length).toBe(1);
      expect(categories[0]).toEqual(mockCategory);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/product-category');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should fetch a product by ID', () => {
    service.getProduct(1).subscribe(product => {
      expect(product).toEqual(mockProduct);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/products/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockProduct);
  });

  it('should handle product stock updates', () => {
    // Initial state
    expect(service.getProductStock('1')).toBeUndefined();

    // Update stock
    service.updateProductStock('1', 20);
    expect(service.getProductStock('1')).toBe(20);

    // Update stock again
    service.updateProductStock('1', 15);
    expect(service.getProductStock('1')).toBe(15);

    // Add a new product stock
    service.updateProductStock('2', 30);
    expect(service.getProductStock('2')).toBe(30);
  });

  it('should handle stock availability observable', () => {
    let availability: { id: string; units: number }[] = [];
    service.availability$.subscribe(data => {
      availability = data;
    });

    service.updateProductStock('1', 10);
    expect(availability).toEqual([{ id: '1', units: 10 }]);

    service.updateProductStock('2', 20);
    expect(availability).toEqual([
      { id: '1', units: 10 },
      { id: '2', units: 20 }
    ]);
  });

  it('should fetch paginated products by keyword', () => {
    const mockResponse = {
      _embedded: { products: [mockProduct] },
      page: {
        size: 10,
        totalElements: 1,
        totalPages: 1,
        number: 0
      }
    };

    service.getProductsSearchPagination(0, 10, 'test', 'name').subscribe(response => {
      expect(response._embedded.products.length).toBe(1);
      expect(response._embedded.products[0]).toEqual(mockProduct);
    });

    const req = httpMock.expectOne(
      'http://localhost:8080/api/products/search/findByNameContaining?name=test&page=0&size=10&sort=name'
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });
});
