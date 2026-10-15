import { Component, OnInit } from '@angular/core';
// Standalone product creation page: form fields for product, images and categories.
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
import { NgForOf, NgIf } from '@angular/common';
import { ProductImagePipe } from '../../pipes/product-image.pipe';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  imports: [ReactiveFormsModule, NgForOf, NgIf, ProductImagePipe, RouterLink],
  styleUrls: ['./add-product.component.css'],
  standalone: true,
})
export class AddProductComponent implements OnInit {
  addProductFormGroup!: FormGroup;
  categories: ProductCategory[] = [];
  isToUpdate: boolean = false;
  productIdToUpdate: number = 0;


  constructor(private productService: ProductService, private route:ActivatedRoute,private router:Router,private http:HttpClient,private categoryService:CategoryService) {}

  ngOnInit() {
  // Service wiring: product, route, http, category.
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
      imageUrl: new FormControl(''),
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
    this.categoryService
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
  // Category getter from the form control.
  get category() {
    return this.addProductFormGroup.get('category');
  }

  // Product name getter from the form control.
  get name() {
    return this.addProductFormGroup.get('name');
  }

  // Product description getter from the form control.
  get description() {
    return this.addProductFormGroup.get('description');
  }

  // Image URL getter from the form control.
  get imageUrl() {
    return this.addProductFormGroup.get('imageUrl');
  }
  get unitPrice() {
  // Product image URL getter from the form control.
    return this.addProductFormGroup.get('unitPrice');
  }
  get unitsInStock() {
  // Product unit price getter from the form control.
    return this.addProductFormGroup.get('unitsInStock');
  }

  onSubmit() {
  // Product units in stock getter from the form control.
    if (this.addProductFormGroup.invalid) {
      this.addProductFormGroup.markAllAsTouched();
      return;
    }
    let name = this.addProductFormGroup.controls['name'].value;
    let description = this.addProductFormGroup.controls['description'].value;
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
    // Uploads a pending image first, then saves the product with the final URL.
    this.resolveImageUrl((imageUrl) => {
      const addProduct = {
        categoryId: category[0].id,
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
          this.router.navigate(['/products']);
        },
        error: (err) => {
          console.error('Error adding product:', err);
          alert(`There was an error adding the product: ${err.message}`);
        },
      });
    }
    });

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
    this.existingImageUrl = data.imageUrl;
      // fetch and patch category data into the form
      const categoryUrl = data._links.category.href
      this.categoryService.getProductCategory(categoryUrl).subscribe(data=>{
        this.addProductFormGroup.patchValue({
          category: data.categoryName
        })
      })

    });


  }
  // File upload members.
  selectedFile: File | null = null;
  existingImageUrl: string | null = null;
  imageRemoved: boolean = false;
  previewUrl: string | null = null;
  imageError: string | null = null;
  isUploading: boolean = false;
  private previewObjectUrl: string | null = null;
  private readonly allowedTypes = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];
  private readonly maxFileSize = 5 * 1024 * 1024;

  // Shows the pending preview, the just-picked file, or the stored image — null when removed.
  get effectiveImage(): string | null {
    if (this.imageRemoved) {
      return null;
    }
    if (this.previewUrl) {
      return this.previewUrl;
    }
    if (this.selectedFile) {
      return this.previewUrl;
    }
    return this.existingImageUrl;
  }

  // Handles the file input change: validates type/size and creates a blob preview.
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files && input.files[0];
    if (!file) {
      return;
    }
    if (!this.allowedTypes.includes(file.type)) {
      this.selectedFile = null;
      this.clearPreview();
      this.imageError = 'Only JPEG, PNG, WEBP or GIF images are allowed.';
      return;
    }
    if (file.size > this.maxFileSize) {
      this.selectedFile = null;
      this.clearPreview();
      this.imageError = 'Image must be 5 MB or smaller.';
      return;
    }
    this.selectedFile = file;
    this.imageRemoved = false;
    this.imageError = null;
    this.clearPreview();
    this.previewObjectUrl = URL.createObjectURL(file);
    this.previewUrl = this.previewObjectUrl;
  }

  // Clears the pending image and its preview so a replaced photo is fresh.
  removeImage(): void {
    this.selectedFile = null;
    this.existingImageUrl = null;
    this.imageRemoved = true;
    this.imageError = null;
    this.clearPreview();
  }

  // Revokes the blob preview URL to avoid leaking object URLs.
  private clearPreview(): void {
    if (this.previewObjectUrl) {
      URL.revokeObjectURL(this.previewObjectUrl);
      this.previewObjectUrl = null;
    }
    this.previewUrl = null;
  }

  // Resolves the final image URL: uploads the pending file, keeps the stored one, or sends ''.
  private resolveImageUrl(done: (url: string) => void): void {
    if (this.selectedFile) {
      this.isUploading = true;
      this.productService.uploadImage(this.selectedFile).subscribe({
        next: (res) => {
          this.isUploading = false;
          done(res.url);
        },
        error: (err) => {
          this.isUploading = false;
          this.imageError = `Image upload failed: ${err.message ?? err}`;
        },
      });
      return;
    }
    if (this.imageRemoved || !this.existingImageUrl) {
      done('');
      return;
    }
    done(this.existingImageUrl);
  }

}
