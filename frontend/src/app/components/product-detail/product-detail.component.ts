import {Component, OnInit} from '@angular/core';
import {Product} from "../../common/product";
import {ProductService} from "../../services/product.service";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {CurrencyPipe, NgIf, NgOptimizedImage} from "@angular/common";
import {CartItem} from "../../common/cart-item";
import {CartService} from "../../services/cart.service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLink,
    NgIf,
  ],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit{
  product!:Product;
  constructor(private productService:ProductService,private cartService:CartService,private route:ActivatedRoute,private authService:AuthService) {
  }
  ngOnInit() {
    this.route.paramMap.subscribe(value => this.getProductDetail())
  }
  getRole() {
    return this.authService.getRole();
  }

  private getProductDetail() {
    const productId:number = +this.route.snapshot.paramMap.get('id')!
    this.productService.getProduct(productId).subscribe(data=>this.product=data)
  }


  addToCart() {
    const cartItem = new CartItem(this.product)
    this.cartService.addToCart(cartItem)
  }
}
