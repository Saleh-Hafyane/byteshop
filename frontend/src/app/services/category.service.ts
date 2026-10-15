// Reads and edits product categories; admin writes carry the JWT in their headers.
import { Injectable } from '@angular/core';
import {map, Observable, Subject} from "rxjs";
import {ProductCategory} from "../common/product-category";
import {HttpClient, HttpHeaders} from "@angular/common/http";

// Provided in root so the header and admin pages share one instance.
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private categoryUrl = 'http://localhost:8080/api/product-category'; // backend category endpoints
  constructor(private httpClient:HttpClient) { }
  // Fires whenever an admin adds a category, so open lists can refresh themselves.
  private categoryAddedSubject = new Subject<void>();
  categoryAdded$ = this.categoryAddedSubject.asObservable();

  // Signals subscribers that the category list has changed.
  notifyCategoryAdded() {
    this.categoryAddedSubject.next();
  }
  // GET the category list, unwrapping the Spring Data REST _embedded envelope.
  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient
      .get<GetResponseProductCategory>(this.categoryUrl)
      .pipe(map((response) => response._embedded.productCategory));
  }
  // GET one category by the link URL the backend handed out.
  getProductCategory(categoryUrl: string): Observable<ProductCategory> {
    return  this.httpClient.get<ProductCategory>(categoryUrl)
  }
  // POST /add — admin only, so the JWT rides along in the headers.
  addProductCategory(newCategoryData: any): Observable<ProductCategory> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.post<any>(this.categoryUrl+"/add", newCategoryData,
      {
        headers
      })

  }

  // DELETE /remove/{id} — admin only.
  removeProductCategory(id: number): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.delete<any>(`${this.categoryUrl}/remove/${id}`, { headers });
  }

  // PUT /update/{id} — renames a category, admin only.
  updateProductCategory(id: number, categoryName: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.httpClient.put<any>(`${this.categoryUrl}/update/${id}`, { categoryName }, { headers });
  }

}
// Shape of the Spring Data REST list response for categories.
interface GetResponseProductCategory {
  _embedded: {
    productCategory: ProductCategory[];
  };
}
