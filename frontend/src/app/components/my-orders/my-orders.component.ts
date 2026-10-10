import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf, DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { ORDER_STATUS_BADGE, OrderStatus, OrderSummary } from '../../common/order-summary';

@Component({
  selector: 'app-my-orders',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DatePipe,
    DecimalPipe,
    RouterLink
  ],
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.css'
})
export class MyOrdersComponent implements OnInit {
  orders: OrderSummary[] = [];
  isLoading = true;
  loadError: string | null = null;

  constructor(private orderService: OrderService) {
  }

  ngOnInit() {
    this.getOrders();
  }

  getOrders() {
    this.isLoading = true;
    this.loadError = null;
    this.orderService.getMyOrders().subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (e) => {
        console.error('Error loading my orders:', e);
        this.loadError = 'Failed to load your orders. Please try again.';
        this.isLoading = false;
      }
    });
  }

  badgeClass(status: OrderStatus): string {
    return ORDER_STATUS_BADGE[status] ?? 'bg-secondary';
  }
}
