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
  styleUrl: './product-category-sm.component.css'
})
export class ProductCategorySmComponent implements OnInit {
  menuActive = false;
  productCategories:ProductCategory[]=[]
  constructor(private categoryService:CategoryService) {

  }
  ngOnInit() {
    this.productCategoryList()
  }


  private productCategoryList() {
    this.categoryService.getProductCategories().subscribe(data=>{

      this.productCategories=data});
  }

  toggleMenu() {
    this.menuActive = !this.menuActive;
  }
}
