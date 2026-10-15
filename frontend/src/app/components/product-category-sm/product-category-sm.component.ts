// Standalone presentational component: small compact category list (topbar dropdown).
import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {ProductCategory} from "../../common/product-category";
import {ProductService} from "../../services/product.service";
import {NgForOf} from "@angular/common";
import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-product-category-sm',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    RouterLinkActive
  ],
  templateUrl: './product-category-sm.component.html',
  styleUrl: './product-category-sm.component.css',
})
export class ProductCategorySmComponent implements OnInit {
  menuActive = false;
  productCategories:ProductCategory[]=[]
  // Injected service for loading categories.
  constructor(private categoryService:CategoryService) {

  }
  // Loads the category list when the component initializes.
// Loads the category list when the component initializes.

  ngOnInit() {
    this.productCategoryList()
// Fetches and stores the category list.

  }

  private productCategoryList() {
    this.categoryService.getProductCategories().subscribe(data=>{
// Toggles the compact dropdown menu open or closed.


      this.productCategories=data});
  }

  toggleMenu() {
    this.menuActive = !this.menuActive;
  }
}
