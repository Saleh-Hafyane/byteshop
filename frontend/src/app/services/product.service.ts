// Catalog reads (paged lists, search, detail) plus admin CRUD and image upload, with a live stock map.
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, map, Observable } from 'rxjs';
import { Product } from '../common/product';
import { ProductCategory } from '../common/product-category';
// Provided in root so the cart and product pages share one stock map.
@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // Public catalog endpoints vs admin-only ones.
  private baseUrl = 'http://localhost:8080/api/products';
  private baseAdminUrl = 'http://localhost:8080/api/admin/products';

  // Holds remaining stock per product id while the app is open; drives cart availability checks.
  private availabilitySource = new BehaviorSubject<
    { id: string; units: number }[]
  >([]);
  availability$ = this.availabilitySource.asObservable();

  constructor(private httpClient: HttpClient) {
    // Start with no stock information at all.
    this.availabilitySource.next([]);
  }

  // Paged product list, filtered by category (-1 means all categories) or sorted freely.
  getProductsPagination(
    page: number,
    pageSize: number,
    categoryId: number,
    sort: string
  ): Observable<GetResponseProduct> {
    let searchUrl: string = '';

    // Empty name on purpose: the "all" view just pages through everything.
    if (categoryId === -1) {
      searchUrl = `${this.baseUrl}/search/findByNameContaining?name=&page=${page}&size=${pageSize}&sort=${sort}`;
    } else {
      searchUrl = `${this.baseUrl}/search/findByCategoryId?id=${categoryId}&page=${page}&size=${pageSize}&sort=${sort}`;
    }

    return this.httpClientProduct(searchUrl);
  }



  // Paged keyword search on the product name, used by the search box.
  getProductsSearchPagination(
    page: number,
    pageSize: number,
    keyword: string,
    sort: string
  ): Observable<GetResponseProduct> {
    const searchUrl = `${this.baseUrl}/search/findByNameContaining?name=${keyword}&page=${page}&size=${pageSize}&sort=${sort}`;
    return this.httpClientProduct(searchUrl);
  }

  // Single product for the detail page.
  getProduct(productId: number): Observable<Product> {
    const productUrl = `${this.baseUrl}/${productId}`;
    return this.httpClient.get<Product>(productUrl);
  }


  // Shared GET used by both paged list calls above.
  httpClientProduct(url: string): Observable<GetResponseProduct> {
    return this.httpClient.get<GetResponseProduct>(url);
  }

  // Sets the remaining units for one product and notifies every subscriber.
  updateProductStock(productId: string, unitsInStock: number) {
    const currentAvailability = this.availabilitySource.getValue();

    const product = currentAvailability.find((item) => item.id === productId);

    // Known product gets updated, unknown one is added as a new entry.
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

  // Remaining units for a product, undefined until the cart first touches it.
  getProductStock(productId: string): number | undefined {
    const product = this.availabilitySource
      .getValue()
      .find((item) => item.id === productId);
    return product?.units;
  }
  // POST /api/admin/products/add — admin only, JWT sent manually here.
  addProduct(product: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.post<any>(this.baseAdminUrl + '/add', product, {
      headers,
    });
  }
  // PUT /api/admin/products/{id} — admin only, overwrites the product's fields.
  updateProduct(id: number, product: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.put<any>(`${this.baseAdminUrl}/${id}`, product,
      {
        headers
      });
  }
  // POST /api/admin/products/upload — sends the file as multipart, gets back { url: '/api/files/...' }.
  uploadImage(file: File): Observable<{ url: string }> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.httpClient.post<{ url: string }>(`${this.baseAdminUrl}/upload`, formData, {
      headers,
    });
  }
  // DELETE /api/admin/products/{id} — admin only.
  deleteProduct(id: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.delete<any>(`${this.baseAdminUrl}/${id}`, {
      headers,
    });
  }


}

// Shape of the paged Spring Data REST product list response.
interface GetResponseProduct {
  _embedded: {
    products: Product[];
  };
  page: {
    size: number; // requested page size
    totalElements: number; // total matching products
    totalPages: number; // total pages, used by the pagination component
    number: number; // current page index
  };
}


