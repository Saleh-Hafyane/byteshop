import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, map, Observable } from "rxjs";
import { Product } from "../common/product";
import { ProductCategory } from "../common/product-category";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  // API endpoints for products and product categories
  private baseUrl = "http://localhost:8080/api/products";
  private categoryUrl = "http://localhost:8080/api/product-category";

  // BehaviorSubject to hold and broadcast product stock availability
  private availabilitySource = new BehaviorSubject<{ id: string, units: number }[]>([]);
  availability$ = this.availabilitySource.asObservable();

  constructor(private httpClient: HttpClient) {
    // Initialize availabilitySource with an empty array
    this.availabilitySource.next([]);
  }

  /**
   * Retrieves paginated products based on page number, page size, and category ID.
   * @param page - The current page number.
   * @param pageSize - Number of products per page.
   * @param categoryId - ID of the category to filter products.
   * @returns Observable of paginated product data.
   */
  getProductsPagination(page: number, pageSize: number, categoryId: number): Observable<GetResponseProduct> {
    let searchUrl: string = "";

    // Check if all categories are selected (-1); otherwise, filter by category ID
    if (categoryId === -1) {
      searchUrl = `${this.baseUrl}/search/findByNameContaining?name=&page=${page}&size=${pageSize}`;
    } else {
      searchUrl = `${this.baseUrl}/search/findByCategoryId?id=${categoryId}&page=${page}&size=${pageSize}`;
    }

    return this.httpClientProduct(searchUrl);
  }

  /**
   * Retrieves the list of product categories.
   * @returns Observable of product categories.
   */
  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient.get<GetResponseProductCategory>(this.categoryUrl)
      .pipe(map(response => response._embedded.productCategory));
  }

  /**
   * Retrieves paginated products based on a search keyword.
   * @param page - The current page number.
   * @param pageSize - Number of products per page.
   * @param keyword - The search term for products.
   * @returns Observable of paginated product data.
   */
  getProductsSearchPagination(page: number, pageSize: number, keyword: string): Observable<GetResponseProduct> {
    const searchUrl = `${this.baseUrl}/search/findByNameContaining?name=${keyword}&page=${page}&size=${pageSize}`;
    return this.httpClientProduct(searchUrl);
  }

  /**
   * Retrieves a specific product by ID.
   * @param productId - The ID of the product to retrieve.
   * @returns Observable of the specified product.
   */
  getProduct(productId: number): Observable<Product> {
    const productUrl = `${this.baseUrl}/${productId}`;
    return this.httpClient.get<Product>(productUrl);
  }

  /**
   * Helper function to perform HTTP GET requests for product data.
   * @param url - The endpoint URL.
   * @returns Observable of paginated product data.
   */
  httpClientProduct(url: string): Observable<GetResponseProduct> {
    return this.httpClient.get<GetResponseProduct>(url);
  }

  /**
   * Updates the stock availability of a product and syncs it across the app.
   * @param productId - The ID of the product to update.
   * @param unitsInStock - The new stock quantity.
   */
  updateProductStock(productId: string, unitsInStock: number) {
    // Get the current list of available products
    const currentAvailability = this.availabilitySource.getValue();

    // Find the product in the availability list by ID
    const product = currentAvailability.find(item => item.id === productId);

    // Update the stock if product exists; otherwise, add it to the list
    if (product) {
      product.units = unitsInStock;
      this.availabilitySource.next([...currentAvailability]);
    } else {
      this.availabilitySource.next([...currentAvailability, { id: productId, units: unitsInStock }]);
    }
  }

  /**
   * Retrieves the available stock for a specific product.
   * @param productId - The ID of the product to check.
   * @returns The units in stock or undefined if the product is not found.
   */
  getProductStock(productId: string): number | undefined {
    const product = this.availabilitySource.getValue().find(item => item.id === productId);
    return product?.units;
  }
}

// Interfaces to define the structure of API responses
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

interface GetResponseProductCategory {
  _embedded: {
    productCategory: ProductCategory[];
  };
}
