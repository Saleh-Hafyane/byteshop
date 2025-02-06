import {Component, OnInit} from '@angular/core';

import {ProductCategory} from "../../common/product-category";
import {NgForOf} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-manage-categories',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule
  ],
  templateUrl: './manage-categories.component.html',
  styleUrl: './manage-categories.component.css'
})
export class ManageCategoriesComponent implements OnInit{
  categories:ProductCategory[] = []
  newCategory: string = '';
  constructor(private categoryService:CategoryService) {
  }
  ngOnInit() {
    this.getCategories()
  }
  getCategories(){
    this.categoryService.getProductCategories().subscribe(data=>{

      this.categories = data
    })

  }

  addCategory() {
    if (!this.newCategory.trim()) {
      alert('Category name cannot be empty!');
      return;
    }

    // Prepare the data to send to the backend
    const newCategoryData = {
      categoryName: this.newCategory
    };

    // Send a POST request to the backend
    this.categoryService.addProductCategory(newCategoryData).subscribe({
        next: () => {
          this.newCategory = ''; // Clear the input field
          this.categoryService.notifyCategoryAdded(); // Notify the category service
          this.getCategories(); // Refresh the list of categories
        },
        error: (e) => {
          console.error('Error adding category:', e);
          alert('Failed to add category. Please try again.');
        },
        complete: () => console.log('Completed')
      }
      );
  }

  remCategory(id: number) {
    this.categoryService.removeProductCategory(id).subscribe({
      next: () => {
        this.categoryService.notifyCategoryAdded(); // Notify the category service
        this.getCategories(); // Refresh the list of categories
      },
      error: (e) => {
        console.error('Error removing category:', e);
        alert('Failed to remove category. Please try again.');
      },
      complete: () => console.log('Completed')
    })

  }
}
