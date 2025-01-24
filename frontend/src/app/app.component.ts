import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {ProductListComponent} from "./components/product-list/product-list.component";
import {HttpClientModule} from "@angular/common/http";
import {ProductCategoryComponent} from "./components/product-category/product-category.component";
import {SearchComponent} from "./components/search/search.component";
import {CartStatusComponent} from "./components/cart-status/cart-status.component";
import {ReactiveFormsModule} from "@angular/forms";
import {LoginHeaderComponent} from "./components/login-header/login-header.component";
import {ProductCategorySmComponent} from "./components/product-category-sm/product-category-sm.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ProductListComponent, RouterLink, RouterLinkActive, ProductCategoryComponent, SearchComponent, CartStatusComponent, ReactiveFormsModule, LoginHeaderComponent, ProductCategorySmComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
