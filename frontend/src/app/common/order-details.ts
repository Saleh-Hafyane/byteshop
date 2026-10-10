import { OrderSummary } from './order-summary';

/** Single purchased line mirroring the backend OrderItemDTO (response shape). */
export interface OrderItemDetail {
  imageUrl: string;
  unitPrice: number;
  quantity: number;
  productId: number;
  productName: string | null;
}

/** Shipping address snapshot mirroring the backend AddressDTO. */
export interface OrderAddress {
  city: string;
  fullAddress: string;
}

/** Full read-model mirroring the backend OrderDetailsDTO. */
export interface OrderDetails extends OrderSummary {
  address: OrderAddress | null;
  orderItems: OrderItemDetail[];
}
