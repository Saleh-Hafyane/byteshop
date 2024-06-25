import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Product} from "../common/product";
import {ProductCategory} from "../common/product-category";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUrl = "http://localhost:8080/api/products";
  private categoryUrl  = "http://localhost:8080/api/product-category"

  constructor(private httpClient:HttpClient) { }
  getProducts(categoryId:number):Observable<Product[]>{
    const searchUrl = this.baseUrl+"/search/findByCategoryId?id="+categoryId
    return this.getProductsData(searchUrl);
  }
  getProductsPagination(page:number,pageSize:number,categoryId:number):Observable<GetResponseProduct>{
    const searchUrl = this.baseUrl+"/search/findByCategoryId?id="+categoryId+"&page="+page+"&size="+pageSize
    return this.httpClient.get<GetResponseProduct>(searchUrl);
  }


  getProductCategories():Observable<ProductCategory[]> {
    return this.httpClient.get<GetResponseProductCategory>(this.categoryUrl).pipe(map(response=>response._embedded.productCategory))

  }

  getProductsSearch(keyword:string) :Observable<Product[]>{
    const searchUrl = this.baseUrl+"/search/findByNameContaining?name="+keyword
    return this.getProductsData(searchUrl);
  }
  getProductsSearchPagination(page:number,pageSize:number,keyword:string):Observable<GetResponseProduct>{
    const searchUrl = this.baseUrl+"/search/findByNameContaining?name="+keyword+"&page="+page+"&size="+pageSize
    return this.httpClient.get<GetResponseProduct>(searchUrl);
  }

  private getProductsData(searchUrl: string) {
    return this.httpClient.get<GetResponseProduct>(searchUrl).pipe(map(response => response._embedded.products))
  }

  getProduct(productId: number):Observable<Product> {
    const productUrl = this.baseUrl+"/"+productId
    return this.httpClient.get<Product>(productUrl)
  }
}
interface GetResponseProduct{
  _embedded:{
    products:Product[];
  }
  page:{
   size:number,
   totalElements:number,
   totalPages:number,
    number:number
}
}
interface GetResponseProductCategory{
  _embedded:{
    productCategory:ProductCategory[];
  }
}
