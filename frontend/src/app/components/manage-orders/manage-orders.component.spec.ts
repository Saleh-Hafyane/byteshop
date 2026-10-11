import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { ManageOrdersComponent } from './manage-orders.component';
import { OrderService } from '../../services/order.service';
import { OrderSummary } from '../../common/order-summary';

describe('ManageOrdersComponent', () => {
  let component: ManageOrdersComponent;
  let fixture: ComponentFixture<ManageOrdersComponent>;
  let orderService: jasmine.SpyObj<OrderService>;
  let router: jasmine.SpyObj<Router>;

  const buildOrder = (): OrderSummary => ({
    id: 21,
    orderTrackingNumber: '8f14e45f-ceea-467f-a1d6-1f6ef4b7f8f2',
    status: 'PENDING',
    totalQuantity: 2,
    totalPrice: 249.97,
    dateCreated: '2026-10-01T10:00:00',
    customerUsername: 'janedoe',
  });

  beforeEach(async () => {
    orderService = jasmine.createSpyObj('OrderService', ['getAdminOrders', 'updateOrderStatus']);
    orderService.getAdminOrders.and.returnValue(of([buildOrder()]));
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [ManageOrdersComponent],
      providers: [
        { provide: OrderService, useValue: orderService },
        { provide: Router, useValue: router },
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit -> loads the admin order list

    spyOn(window, 'alert');
  });

  it('loads the admin order list on init', () => {
    expect(orderService.getAdminOrders).toHaveBeenCalled();
    expect(component.orders.length).toBe(1);
    expect(component.orders[0].status).toBe('PENDING');
    expect(component.isLoading).toBeFalse();
    expect(component.loadError).toBeNull();
  });

  it('updates the row only after the server confirms the new status', () => {
    const updated: OrderSummary = { ...buildOrder(), status: 'SHIPPED' };
    orderService.updateOrderStatus.and.returnValue(of(updated));

    component.changeStatus(component.orders[0], 'SHIPPED');

    expect(orderService.updateOrderStatus).toHaveBeenCalledWith(21, 'SHIPPED');
    expect(component.orders[0].status).toBe('SHIPPED');
    expect(component.isUpdating(component.orders[0])).toBeFalse();
  });

  it('keeps the old status and shows the backend message when the PATCH fails', () => {
    orderService.updateOrderStatus.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 500, error: { message: 'Order not found' } }))
    );

    component.changeStatus(component.orders[0], 'SHIPPED');

    // Pessimistic update: a failed PATCH must never flash a phantom status
    expect(component.orders[0].status).toBe('PENDING');
    expect(window.alert).toHaveBeenCalledWith('Failed to update order status: Order not found');
    expect(component.isUpdating(component.orders[0])).toBeFalse();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('alerts and redirects to /login when the session has expired (401)', () => {
    orderService.updateOrderStatus.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 401 }))
    );

    component.changeStatus(component.orders[0], 'SHIPPED');

    expect(component.orders[0].status).toBe('PENDING');
    expect(window.alert).toHaveBeenCalledWith('Your session has expired. Please log in again.');
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.isUpdating(component.orders[0])).toBeFalse();
  });

  it('does not call the API when the selected status equals the current one', () => {
    component.changeStatus(component.orders[0], 'PENDING');

    expect(orderService.updateOrderStatus).not.toHaveBeenCalled();
    expect(component.orders[0].status).toBe('PENDING');
  });
});