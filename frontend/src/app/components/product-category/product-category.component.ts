import {Component, OnInit} from '@angular/core';
import {ProductCategory} from "../../common/product-category";
import {ProductService} from "../../services/product.service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-product-category',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    NgForOf
  ],
  templateUrl: './product-category.component.html',
  styleUrl: './product-category.component.css'
})
export class ProductCategoryComponent implements OnInit{
  productCategories:ProductCategory[]=[]
  constructor(private productService:ProductService) {

  }
  ngOnInit() {
    this.productCategoryList()
  }


  private productCategoryList() {
    this.productService.getProductCategories().subscribe(data=>{

      this.productCategories=data});
  }

  protected readonly ProductCategory = ProductCategory;
}
