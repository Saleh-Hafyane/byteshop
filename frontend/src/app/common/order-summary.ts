// Order status values shared by the lists, badges and the admin status dropdown.
export type OrderStatus = 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

// Every selectable status, in display order.
export const ORDER_STATUSES: OrderStatus[] = [
  'PENDING',
  'CONFIRMED',
  'SHIPPED',
  'DELIVERED',
  'CANCELLED'
];

// One row of an order list, matching the backend OrderSummaryDTO exactly.
export interface OrderSummary {
  id: number; // order id, opens the details page
  orderTrackingNumber: string; // public tracking UUID
  status: OrderStatus; // current lifecycle state
  totalQuantity: number; // number of items
  totalPrice: number; // amount of the order
  dateCreated: string; // placement date, used for sorting
  customerUsername: string; // who placed the order
}

// Bootstrap badge classes per status for the admin table and user history.
export const ORDER_STATUS_BADGE: Record<OrderStatus, string> = {
  PENDING: 'bg-warning text-dark', // yellow — not processed yet
  CONFIRMED: 'bg-info text-dark', // blue-ish — accepted
  SHIPPED: 'bg-primary', // solid blue — on the way
  DELIVERED: 'bg-success', // green — completed
  CANCELLED: 'bg-danger' // red — stopped
};
