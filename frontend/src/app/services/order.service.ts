import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderSummary, OrderStatus } from '../common/order-summary';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private adminOrdersUrl = 'http://localhost:8080/api/admin/orders';

  constructor(private httpClient: HttpClient) { }

  /** Retrieves all orders (admin only). Auth header is attached by JwtInterceptorService. */
  getAdminOrders(): Observable<OrderSummary[]> {
    return this.httpClient.get<OrderSummary[]>(this.adminOrdersUrl);
  }

  /** Transitions an order to a new status; returns the updated summary. */
  updateOrderStatus(id: number, status: OrderStatus): Observable<OrderSummary> {
    return this.httpClient.patch<OrderSummary>(`${this.adminOrdersUrl}/${id}/status`, { status });
  }
}
