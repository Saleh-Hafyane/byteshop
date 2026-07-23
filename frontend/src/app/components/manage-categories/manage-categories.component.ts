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

  ngOnInit() {
    this.getCategories();
  }

  getCategories() {
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
      error: (e) => {
        console.error('Error adding category:', e);
        alert('Failed to add category. Please try again.');
      }
    });
  }

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
