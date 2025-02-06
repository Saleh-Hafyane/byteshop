import {Component, OnInit} from '@angular/core';
import {ProductCategory} from "../../common/product-category";
import {ProductService} from "../../services/product.service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgForOf} from "@angular/common";
import {CategoryService} from "../../services/category.service";

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
  constructor(private categoryService:CategoryService) {

  }
  ngOnInit() {
    this.productCategoryList() // initialize the list of categories
    this.categoryService.categoryAdded$.subscribe(() => {
      this.productCategoryList(); // Refresh when notified
    });
  }


  private productCategoryList() {
    this.categoryService.getProductCategories().subscribe(data=>{

      this.productCategories=data});
  }

}
