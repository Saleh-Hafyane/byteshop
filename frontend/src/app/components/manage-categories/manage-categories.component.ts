// Standalone admin page: list, add, edit and remove product categories with a reactive form.

import {Component, OnInit} from '@angular/core';
import {ProductCategory} from "../../common/product-category";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {CategoryService} from "../../services/category.service";

@Component({
  selector: 'app-manage-categories',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './manage-categories.component.html',
  styleUrl: './manage-categories.component.css'
})
export class ManageCategoriesComponent implements OnInit {
  categories: ProductCategory[] = [];
  newCategory: string = '';
  editingCategoryId: number | null = null;
  editingCategoryName: string = '';

  constructor(private categoryService: CategoryService, private http: HttpClient, private router: Router) {
  }
// Loads the category list when the component initializes.


  ngOnInit() {
// Fetches the category list and stores it.

    this.getCategories();
  }

  getCategories() {
// Adds a new category after basic validation.

    this.categoryService.getProductCategories().subscribe(data => {
      this.categories = data;
    });
  }

  addCategory() {
    if (!this.newCategory.trim()) {
      alert('Category name cannot be empty!');
      return;
    }
    const newCategoryData = {categoryName: this.newCategory};
    this.categoryService.addProductCategory(newCategoryData).subscribe({
      next: () => {
        this.newCategory = '';
        this.categoryService.notifyCategoryAdded();
        this.getCategories();
      },
// Prepares the given category for editing.

      error: (e) => {
        console.error('Error adding category:', e);
        alert('Failed to add category. Please try again.');
// Cancels the current edit and resets the form state.

      }
    });
  }
// Persists the edited category name and refreshes the list.


  startEdit(category: ProductCategory) {
    this.editingCategoryId = category.id;
    this.editingCategoryName = category.categoryName;
  }

  cancelEdit() {
    this.editingCategoryId = null;
    this.editingCategoryName = '';
  }

  saveEdit(id: number) {
    if (!this.editingCategoryName.trim()) {
      alert('Category name cannot be empty!');
      return;
    }
    this.categoryService.updateProductCategory(id, this.editingCategoryName).subscribe({
// Deletes a category and refreshes the list.

      next: () => {
        this.editingCategoryId = null;
        this.editingCategoryName = '';
        this.categoryService.notifyCategoryAdded();
        this.getCategories();
      },
      error: (e) => {
        console.error('Error updating category:', e);
        alert('Failed to update category. Please try again.');
      }
    });
  }

  remCategory(id: number) {
    this.categoryService.removeProductCategory(id).subscribe({
      next: () => {
        this.categoryService.notifyCategoryAdded();
        this.getCategories();
      },
      error: (e) => {
        console.error('Error removing category:', e);
        alert('Failed to remove category. Please try again.');
      }
    });
  }
}
