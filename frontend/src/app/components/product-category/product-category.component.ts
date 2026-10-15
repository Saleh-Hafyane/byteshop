// Standalone presentational component: horizontal category list with a toggleable dropdown.
import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {ProductCategory} from "../../common/product-category";
import {ProductService} from "../../services/product.service";
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
  styleUrl: './product-category.component.css',
})
export class ProductCategoryComponent implements OnInit{
  productCategories:ProductCategory[]=[]
  // Category service injected via constructor.
  constructor(private categoryService:CategoryService) {

  }
  // Loads categories and refreshes them when a new one is added.
// Loads categories and refreshes them when a new one is added.

  ngOnInit() {
    this.productCategoryList() // initialize the list of categories
    this.categoryService.categoryAdded$.subscribe(() => {
      this.productCategoryList(); // Refresh when notified
    });
  }
// Fetches and stores the category list.



  private productCategoryList() {
    this.categoryService.getProductCategories().subscribe(data=>{

      this.productCategories=data});
  }

}
