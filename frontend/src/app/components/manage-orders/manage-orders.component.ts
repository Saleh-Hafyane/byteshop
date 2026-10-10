import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf, DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
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

  constructor(private orderService: OrderService) {
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
    const previousStatus = order.status;
    this.updatingIds.add(order.id);
    this.orderService.updateOrderStatus(order.id, newStatus).subscribe({
      next: (updated) => {
        const index = this.orders.findIndex(o => o.id === updated.id);
        if (index !== -1) {
          this.orders[index] = updated;
        }
        this.updatingIds.delete(order.id);
      },
      error: (e) => {
        console.error('Error updating order status:', e);
        order.status = previousStatus;
        this.updatingIds.delete(order.id);
        alert('Failed to update order status. Please try again.');
      }
    });
  }
}
