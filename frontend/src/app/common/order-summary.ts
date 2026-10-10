export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export const ORDER_STATUSES: OrderStatus[] = [
  'PENDING',
  'CONFIRMED',
  'SHIPPED',
  'DELIVERED',
  'CANCELLED'
];

/** Flat read-model mirroring the backend OrderSummaryDTO. No nested entities. */
export interface OrderSummary {
  id: number;
  orderTrackingNumber: string;
  status: OrderStatus;
  totalQuantity: number;
  totalPrice: number;
  dateCreated: string;
  customerUsername: string;
}

/** Bootstrap badge class per status for the admin table and user history. */
export const ORDER_STATUS_BADGE: Record<OrderStatus, string> = {
  PENDING: 'bg-warning text-dark',
  CONFIRMED: 'bg-info text-dark',
  SHIPPED: 'bg-primary',
  DELIVERED: 'bg-success',
  CANCELLED: 'bg-danger'
};
