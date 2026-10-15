// Standalone product detail page: loads the product, shows its image/name/prices and adds it to the cart.
import {Component, OnInit} from '@angular/core';
import {Product} from "../../common/product";
import {ProductService} from "../../services/product.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import { CurrencyPipe, NgIf } from "@angular/common";
import {CartItem} from "../../common/cart-item";
import {CartService} from "../../services/cart.service";
import {AuthService} from "../../services/auth.service";
import { ProductImagePipe } from "../../pipes/product-image.pipe";

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLink,
    NgIf,
    ProductImagePipe,
  ],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css',
})
export class ProductDetailComponent implements OnInit{
  product!:Product
  // Services injected via the constructor.
  constructor(private productService:ProductService,private cartService:CartService,private route:ActivatedRoute,private authService:AuthService) {
  }
  // Loads the product from the route parameter when initializing.
  ngOnInit() {
    this.route.paramMap.subscribe(value => this.getProductDetail())
  }

  // Exposes the JWT role claim to the template.
  getRole() {
    return this.authService.getRole();
  }

  // Reads the product from the route id and patches it into the component.
  private getProductDetail() {
    const productId:number = +this.route.snapshot.paramMap.get('id')!

    this.productService.getProduct(productId).subscribe(data=>this.product=data)
  }

  // Creates a cart item and adds it to the cart.
  addToCart() {
    const cartItem = new CartItem(this.product)
    this.cartService.addToCart(cartItem)
  }
}
