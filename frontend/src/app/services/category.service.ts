import { Injectable } from '@angular/core';
import {map, Observable, Subject} from "rxjs";
import {ProductCategory} from "../common/product-category";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private categoryUrl = 'http://localhost:8080/api/product-category';
  constructor(private httpClient:HttpClient) { }
  private categoryAddedSubject = new Subject<void>();
  categoryAdded$ = this.categoryAddedSubject.asObservable();

  notifyCategoryAdded() {
    this.categoryAddedSubject.next();
  }
  // Retrieves the list of product categories.
  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient
      .get<GetResponseProductCategory>(this.categoryUrl)
      .pipe(map((response) => response._embedded.productCategory));
  }
  // retrieve category by category url
  getProductCategory(categoryUrl: string): Observable<ProductCategory> {
    return  this.httpClient.get<ProductCategory>(categoryUrl)
  }
  addProductCategory(newCategoryData: any): Observable<ProductCategory> {
    const token = localStorage.getItem('token'); // Retrieve the JWT token
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.post<any>(this.categoryUrl+"/add", newCategoryData,
      {
        headers
      })

  }

  removeProductCategory(id: number): Observable<any> {
    return this.httpClient.delete<any>(`${this.categoryUrl}/remove/${id}`);

  }
}
// Interface to define the structure of API responses
interface GetResponseProductCategory {
  _embedded: {
    productCategory: ProductCategory[];
  };
}
