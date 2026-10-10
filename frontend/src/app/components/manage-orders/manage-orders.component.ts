import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf, DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { OrderService } from '../../services/order.service';
import { ORDER_STATUSES, ORDER_STATUS_BADGE, OrderStatus, OrderSummary } from '../../common/order-summary';

@Component({
  selector: 'app-manage-orders',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DatePipe,
    DecimalPipe,
    FormsModule
  ],
  templateUrl: './manage-orders.component.html',
  styleUrl: './manage-orders.component.css'
})
export class ManageOrdersComponent implements OnInit {
  orders: OrderSummary[] = [];
  statuses = ORDER_STATUSES;
  isLoading = true;
  loadError: string | null = null;
  updatingIds = new Set<number>();

  constructor(private orderService: OrderService, private router: Router) {
  }

  ngOnInit() {
    this.getOrders();
  }

  getOrders() {
    this.isLoading = true;
    this.loadError = null;
    this.orderService.getAdminOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (e) => {
        console.error('Error loading orders:', e);
        this.loadError = 'Failed to load orders. Please try again.';
        this.isLoading = false;
      }
    });
  }

  badgeClass(status: OrderStatus): string {
    return ORDER_STATUS_BADGE[status] ?? 'bg-secondary';
  }

  isUpdating(order: OrderSummary): boolean {
    return this.updatingIds.has(order.id);
  }

  changeStatus(order: OrderSummary, newStatus: OrderStatus) {
    if (newStatus === order.status || this.isUpdating(order)) {
      return;
    }
    // Pessimistic update: the row keeps the old value until the server
    // confirms, so a failed PATCH never flashes a phantom status.
    this.updatingIds.add(order.id);
    this.orderService.updateOrderStatus(order.id, newStatus).subscribe({
      next: (updated) => {
        const index = this.orders.findIndex(o => o.id === updated.id);
        if (index !== -1) {
          this.orders[index] = updated;
        }
        this.updatingIds.delete(order.id);
      },
      error: (e: HttpErrorResponse) => {
        console.error('Error updating order status:', e);
        this.updatingIds.delete(order.id);
        if (e.status === 401) {
          alert('Your session has expired. Please log in again.');
          this.router.navigate(['/login']);
        } else {
          const detail = e.error?.message ?? 'Please try again.';
          alert(`Failed to update order status: ${detail}`);
        }
      }
    });
  }
}
