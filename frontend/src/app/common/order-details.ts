import { OrderSummary } from './order-summary';

// One purchased line as the backend sends it in order details.
export interface OrderItemDetail {
  imageUrl: string; // product picture at purchase time
  unitPrice: number; // price per unit when bought
  quantity: number; // units bought
  productId: number; // catalog product this line came from
  productName: string | null; // server-side name snapshot
}

// Shipping address snapshot attached to the order details.
export interface OrderAddress {
  city: string; // delivery city
  fullAddress: string; // delivery street line
}

// Full order view for the details page: summary fields plus address and items.
export interface OrderDetails extends OrderSummary {
  address: OrderAddress | null; // may be null on old orders
  orderItems: OrderItemDetail[]; // the purchased lines
}
