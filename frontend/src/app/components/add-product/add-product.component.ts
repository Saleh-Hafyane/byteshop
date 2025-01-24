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
import { ProductManagement } from '../../common/product-management';

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

  constructor(private productService: ProductService) {}

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
      unitInStock: new FormControl('', [
        Validators.required,
        CustomValidators.notOnlySpaces,
        Validators.pattern('^[0-9]+$'),
      ]),
    });
    // getting products categories to populate the form
    this.productService
      .getProductCategories()
      .subscribe((data) => (this.categories = data));
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
  get unitInStock() {
    return this.addProductFormGroup.get('unitInStock');
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
    let unitInStock: number =
      +this.addProductFormGroup.controls['unitInStock'].value;
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
      unitsInStock: unitInStock,
    };

    this.productService.addProduct(addProduct).subscribe({
      next: (Response) => {
        alert('Product added successfully!');
      },
      error: (err) => {
        console.error('Error adding product:', err);
        alert(`There was an error adding the product: ${err.message}`);
      },
    });
  }
}
