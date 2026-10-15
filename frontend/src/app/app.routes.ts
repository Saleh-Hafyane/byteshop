// The single route table: public catalog, guarded customer pages, admin area with adminGuard.
import { Routes } from '@angular/router';
import {ProductListComponent} from "./components/product-list/product-list.component";
import {ProductDetailComponent} from "./components/product-detail/product-detail.component";
import {CartDetailComponent} from "./components/cart-detail/cart-detail.component";
import {CheckoutComponent} from "./components/checkout/checkout.component";
import {authGuard} from "./auth.guard";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import { AddProductComponent } from './components/add-product/add-product.component';
import {ManageCategoriesComponent} from "./components/manage-categories/manage-categories.component";
import {ManageOrdersComponent} from "./components/manage-orders/manage-orders.component";
import {MyOrdersComponent} from "./components/my-orders/my-orders.component";
import {OrderDetailsComponent} from "./components/order-details/order-details.component";
import {adminGuard} from "./admin.guard";


// Checked top to bottom; '*' always falls back to the product list.
export const routes: Routes = [
  // Admin pages — the add/edit form is reused for both create and update via :id.
  {path:'admin/products/add',component:AddProductComponent},
  {path:'admin/products/update/:id',component:AddProductComponent},
  {path:'admin/categories',component:ManageCategoriesComponent},
  {path:'admin/orders',component:ManageOrdersComponent,canActivate:[adminGuard]}, // only ROLE_ADMIN passes
  // Account pages — register and login are public.
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  // Customer pages — any logged-in user passes authGuard.
  {path:'checkout',component:CheckoutComponent,canActivate:[authGuard]},
  {path:'my-orders',component:MyOrdersComponent,canActivate:[authGuard]},
  {path:'my-orders/:id',component:OrderDetailsComponent,canActivate:[authGuard]},
  // Public shopping pages, no guard needed.
  {path:'cart-detail',component:CartDetailComponent},
  {path:'products/:id',component:ProductDetailComponent},
  {path:'search/:keyword',component:ProductListComponent},
  {path:'category/:id',component:ProductListComponent},
  {path:'category',component:ProductListComponent},
  {path:'products',component:ProductListComponent},
  {path:'',redirectTo:'/products',pathMatch:'full'}, // empty URL lands on the catalog
  {path:'**',redirectTo:'/products',pathMatch:'full'} // unknown URLs land on the catalog too
];
