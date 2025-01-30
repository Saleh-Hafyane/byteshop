import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../common/product';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CustomValidators } from '../../validators/customValidators';
import { ProductCategory } from '../../common/product-category';
import { CurrencyPipe, NgForOf, NgIf } from '@angular/common';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  imports: [ReactiveFormsModule, NgForOf, NgIf],
  styleUrls: ['./add-product.component.css'],
  standalone: true,
})
export class AddProductComponent implements OnInit {
  addProductFormGroup!: FormGroup;
  categories: ProductCategory[] = [];
  isToUpdate: boolean = false;
  productIdToUpdate: number = 0;


  constructor(private productService: ProductService, private route:ActivatedRoute,private router:Router,private http:HttpClient) {}

  ngOnInit() {
    this.addProductFormGroup = new FormGroup({
      category: new FormControl('', [Validators.required]),
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        CustomValidators.notOnlySpaces,
      ]),
      description: new FormControl('', [
        Validators.required,
        CustomValidators.notOnlySpaces,
        Validators.minLength(2),
      ]),
      imageUrl: new FormControl('', [
        Validators.required,
        CustomValidators.notOnlySpaces,
        Validators.minLength(2),
      ]),
      unitPrice: new FormControl('', [
        Validators.required,
        CustomValidators.notOnlySpaces,
        Validators.pattern('^[0-9]+(\\.[0-9]{1,2})?$'),
      ]),
      unitsInStock: new FormControl('', [
        Validators.required,
        CustomValidators.notOnlySpaces,
        Validators.pattern('^[0-9]+$'),
      ]),
    });

    // getting products categories to populate the form
    this.productService
      .getProductCategories()
      .subscribe((data) => (this.categories = data));
    // to get the product id to update
    this.route.paramMap.subscribe((params) => {
      this.isToUpdate = this.route.snapshot.paramMap.has('id');
      if (this.isToUpdate) {
        this.productIdToUpdate = Number(this.route.snapshot.paramMap.get('id'));
        if (this.productIdToUpdate) {
          this.getProduct(this.productIdToUpdate);
        }
      }

    })

  }
  get category() {
    return this.addProductFormGroup.get('category');
  }
  get name() {
    return this.addProductFormGroup.get('name');
  }
  get description() {
    return this.addProductFormGroup.get('description');
  }
  get imageUrl() {
    return this.addProductFormGroup.get('imageUrl');
  }
  get unitPrice() {
    return this.addProductFormGroup.get('unitPrice');
  }
  get unitsInStock() {
    return this.addProductFormGroup.get('unitsInStock');
  }

  onSubmit() {
    if (this.addProductFormGroup.invalid) {
      this.addProductFormGroup.markAllAsTouched();
      return;
    }
    let name = this.addProductFormGroup.controls['name'].value;
    let description = this.addProductFormGroup.controls['description'].value;
    let imageUrl = this.addProductFormGroup.controls['imageUrl'].value;
    let unitPrice: number =
      +this.addProductFormGroup.controls['unitPrice'].value;
    let unitsInStock: number =
      +this.addProductFormGroup.controls['unitsInStock'].value;
    let categoryName: String = JSON.parse(
      JSON.stringify(this.addProductFormGroup.controls['category'].value)
    );
    const category = this.categories.filter((c) => {
      return c.categoryName === categoryName;
    });
    const addProduct = {
      category: {
        id: category[0].id,
      },
      name: name,
      description: description,
      unitPrice: unitPrice,
      imageUrl: imageUrl,
      unitsInStock: unitsInStock,
    };
    // if the product to updated or added
    if (this.isToUpdate && this.productIdToUpdate){
      this.productService.updateProduct(this.productIdToUpdate,addProduct).subscribe({
        next: () => {
          alert('Product updated successfully!');
          this.router.navigate(['/products']);

        },
        error: (err) => {
          console.error('Error updating product:', err);
          alert(`There was an error updating the product: ${err.message}`);
        },
      });

    } else {
      this.productService.addProduct(addProduct).subscribe({
        next: () => {
          alert('Product added successfully!');
        },
        error: (err) => {
          console.error('Error adding product:', err);
          alert(`There was an error adding the product: ${err.message}`);
        },
      });
    }

  }


  private getProduct(id:number){
    this.productService.getProduct(id).subscribe((data)=>{
      // patching the data into the form
      this.addProductFormGroup.patchValue({
        name: data.name,
        description: data.description,
        imageUrl: data.imageUrl,
        unitPrice: data.unitPrice.toString(),
        unitsInStock: data.unitsInStock.toString(),

      });
      // fetch and patch category data into the form
      const categoryUrl = data._links.category.href
      this.productService.getProductCategory(categoryUrl).subscribe(data=>{
        this.addProductFormGroup.patchValue({
          category: data.categoryName
        })
      })

    });


  }
}
