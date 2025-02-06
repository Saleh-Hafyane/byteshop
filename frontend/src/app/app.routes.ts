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


export const routes: Routes = [
  {path:'admin/products/add',component:AddProductComponent},
  {path:'admin/products/update/:id',component:AddProductComponent},
  {path:'admin/categories',component:ManageCategoriesComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  {path:'checkout',component:CheckoutComponent,canActivate:[authGuard]},
  {path:'cart-detail',component:CartDetailComponent},
  {path:'products/:id',component:ProductDetailComponent},
  {path:'search/:keyword',component:ProductListComponent},
  {path:'category/:id',component:ProductListComponent},
  {path:'category',component:ProductListComponent},
  {path:'products',component:ProductListComponent},
  {path:'',redirectTo:'/products',pathMatch:'full'},
  {path:'**',redirectTo:'/products',pathMatch:'full'}
];
