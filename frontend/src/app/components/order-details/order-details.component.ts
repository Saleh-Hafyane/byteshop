import { Component, OnInit } from '@angular/core';
// Standalone order detail page: status update with inline enable/disable.
import { ProductImagePipe } from '../../pipes/product-image.pipe';
import { NgForOf, NgIf, DatePipe, DecimalPipe, NgOptimizedImage } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { OrderService } from '../../services/order.service';
import { ORDER_STATUS_BADGE, OrderStatus } from '../../common/order-summary';
import { OrderDetails } from '../../common/order-details';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DatePipe,
    DecimalPipe,
    NgOptimizedImage,
    RouterLink,
    ProductImagePipe
  ],
  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit {
  order: OrderDetails | null = null;
  isLoading = true;
  loadError: string | null = null;

  constructor(private orderService: OrderService, private route: ActivatedRoute) {
  }

  ngOnInit() {
  // Loads the order details from the route id on init.
    this.route.paramMap.subscribe(() => this.getOrderDetails());
  }

  getOrderDetails() {
  // Fetches the order with the given id.
    const id = +(this.route.snapshot.paramMap.get('id')!);
    this.isLoading = true;
    this.loadError = null;
    this.orderService.getOrderDetails(id).subscribe({
      next: (data) => {
        this.order = data;
        this.isLoading = false;
      },
      error: (e: HttpErrorResponse) => {
        console.error('Error loading order details:', e);
        if (e.status === 403) {
          this.loadError = 'You are not allowed to view this order.';
        } else if (e.status === 404) {
          this.loadError = 'Order not found.';
        } else {
          this.loadError = 'Failed to load order details. Please try again.';
        }
        this.isLoading = false;
      }
    });
  }

  badgeClass(status: OrderStatus): string {
    return ORDER_STATUS_BADGE[status] ?? 'bg-secondary';
  }

  itemName(productId: number, productName: string | null): string {
    return productName ?? `Product #${productId}`;
  }
}
