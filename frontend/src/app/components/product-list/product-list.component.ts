import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../common/product";
import {CurrencyPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {NgbPagination} from "@ng-bootstrap/ng-bootstrap";
import {CartService} from "../../services/cart.service";
import {CartItem} from "../../common/cart-item";

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    NgForOf,
    CurrencyPipe,
    NgOptimizedImage,
    NgIf,
    RouterLink,
    NgbPagination
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit {
  products: Product[] = []
  categoryId: number = -1 // id -1 means null
  prevCategoryId: number = 1
  hasKeyword: boolean = false
  prevKeyword: string = ""
  //pagination
  pageNumber: number = 1
  pageSize: number = 5
  totalElements: number = 0




  constructor(private productService: ProductService, private route: ActivatedRoute,private cartService:CartService) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(value => {
      this.productsList()
    })



  }

  productsList() {
    this.hasKeyword = this.route.snapshot.paramMap.has('keyword')
    if (this.hasKeyword) {
      this.handleSearch()
    } else {
      this.handleProductList()
    }
  }

  handleSearch() {
    const keyword = this.route.snapshot.paramMap.get('keyword')!
    if (keyword != this.prevKeyword) {
      this.pageNumber = 1
    }
    this.prevKeyword = keyword
    this.productService.getProductsSearchPagination(this.pageNumber - 1, this.pageSize, keyword).subscribe(this.getResult());
  }

  handleProductList() {
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id')
    if (hasCategoryId) {
      this.categoryId = +this.route.snapshot.paramMap.get('id')!
    }
    //if we have different category than previous restart to first page
    if (this.prevCategoryId != this.categoryId) {
      this.pageNumber = 1
    }
    this.prevCategoryId = this.categoryId

    this.productService.getProductsPagination(this.pageNumber - 1, this.pageSize, this.categoryId)
      .subscribe(this.getResult())
  }


  selectPageSize(value: string) {
    this.pageSize = +value
    this.pageNumber = 1
    this.productsList()

  }

  private getResult() {
    return (data: any) => {
      this.products = data._embedded.products
      this.pageNumber = data.page.number + 1
      this.pageSize = data.page.size
      this.totalElements = data.page.totalElements;
      this.productService.availability$.subscribe(availabilityList => {
        this.products = this.products.map(product => {
          const availabilityItem = availabilityList.find(item => item.id === product.id);

          // If availability data exists, update unitsInStock
          if (availabilityItem) {
            return { ...product, unitsInStock: availabilityItem.units };
          }
          return product;
        });
      });


    }
  }


  addToCart(product: Product) {
    // Create a new CartItem from the product
    const cartItem = new CartItem(product);

    if (!this.cartService.addToCart(cartItem)) {
      alert('Product is out of stock or no more available units!');
    }
  }






}
