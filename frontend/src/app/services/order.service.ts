// All order HTTP calls: admin lists and status updates, my-orders, and details.
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderSummary, OrderStatus } from '../common/order-summary';
import { OrderDetails } from '../common/order-details';

// The JWT interceptor attaches the Authorization header to every call below.
@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private adminOrdersUrl = 'http://localhost:8080/api/admin/orders'; // admin-only endpoints
  private userOrdersUrl = 'http://localhost:8080/api/user/orders'; // caller's own history
  private ordersUrl = 'http://localhost:8080/api/orders'; // details, owner or admin

  constructor(private httpClient: HttpClient) { }

  // GET all orders for the admin table, newest first.
  getAdminOrders(): Observable<OrderSummary[]> {
    return this.httpClient.get<OrderSummary[]>(this.adminOrdersUrl);
  }

  // PATCH /{id}/status — moves an order to the new status and returns the updated row.
  updateOrderStatus(id: number, status: OrderStatus): Observable<OrderSummary> {
    return this.httpClient.patch<OrderSummary>(`${this.adminOrdersUrl}/${id}/status`, { status });
  }

  // GET the logged-in customer's own orders, newest first.
  getMyOrders(): Observable<OrderSummary[]> {
    return this.httpClient.get<OrderSummary[]>(this.userOrdersUrl);
  }

  // GET full details; the backend answers 403 if it isn't your order and you're no admin.
  getOrderDetails(id: number): Observable<OrderDetails> {
    return this.httpClient.get<OrderDetails>(`${this.ordersUrl}/${id}`);
  }
}
