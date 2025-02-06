import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Product } from '../common/product';
import { ProductCategory } from '../common/product-category';
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // API endpoints for products and product categories
  private baseUrl = 'http://localhost:8080/api/products';
  private baseAdminUrl = 'http://localhost:8080/api/admin/products';

  // BehaviorSubject to hold and broadcast product stock availability
  private availabilitySource = new BehaviorSubject<
    { id: string; units: number }[]
  >([]);
  availability$ = this.availabilitySource.asObservable();

  constructor(private httpClient: HttpClient) {
    // Initialize availabilitySource with an empty array
    this.availabilitySource.next([]);
  }

  // Retrieves paginated products based on page number, sort order, page size, and category ID.
  getProductsPagination(
    page: number,
    pageSize: number,
    categoryId: number,
    sort: string
  ): Observable<GetResponseProduct> {
    let searchUrl: string = '';

    // Check if all categories are selected (-1); otherwise, filter by category ID
    if (categoryId === -1) {
      searchUrl = `${this.baseUrl}/search/findByNameContaining?name=&page=${page}&size=${pageSize}&sort=${sort}`;
    } else {
      searchUrl = `${this.baseUrl}/search/findByCategoryId?id=${categoryId}&page=${page}&size=${pageSize}&sort=${sort}`;
    }

    return this.httpClientProduct(searchUrl);
  }



  // Retrieves paginated products based on a search keyword.
  getProductsSearchPagination(
    page: number,
    pageSize: number,
    keyword: string,
    sort: string
  ): Observable<GetResponseProduct> {
    const searchUrl = `${this.baseUrl}/search/findByNameContaining?name=${keyword}&page=${page}&size=${pageSize}&sort=${sort}`;
    return this.httpClientProduct(searchUrl);
  }

  // Retrieves a specific product by ID.
  getProduct(productId: number): Observable<Product> {
    const productUrl = `${this.baseUrl}/${productId}`;
    return this.httpClient.get<Product>(productUrl);
  }


  // Helper function to perform HTTP GET requests for product data.
  httpClientProduct(url: string): Observable<GetResponseProduct> {
    return this.httpClient.get<GetResponseProduct>(url);
  }

  // Updates the stock availability of a product and syncs it across the app.
  updateProductStock(productId: string, unitsInStock: number) {
    // Get the current list of available products
    const currentAvailability = this.availabilitySource.getValue();

    // Find the product in the availability list by ID
    const product = currentAvailability.find((item) => item.id === productId);

    // Update the stock if product exists; otherwise, add it to the list
    if (product) {
      product.units = unitsInStock;
      this.availabilitySource.next([...currentAvailability]);
    } else {
      this.availabilitySource.next([
        ...currentAvailability,
        { id: productId, units: unitsInStock },
      ]);
    }
  }

  // Retrieves the available stock for a specific product.
  getProductStock(productId: string): number | undefined {
    const product = this.availabilitySource
      .getValue()
      .find((item) => item.id === productId);
    return product?.units;
  }
  // add product by the admin
  addProduct(product: any): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve the JWT token
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.post<any>(this.baseAdminUrl + '/add', product, {
      headers,
    });
  }
  // update product by the admin
  updateProduct(id: number, product: any): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve the JWT token
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.put<any>(`${this.baseAdminUrl}/${id}`, product,
      {
        headers
      });
  }
  // delete product by the admin
  deleteProduct(id: number): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve the JWT token
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.delete<any>(`${this.baseAdminUrl}/${id}`, {
      headers,
    });
  }


}

// Interface to define the structure of API responses
interface GetResponseProduct {
  _embedded: {
    products: Product[];
  };
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}


