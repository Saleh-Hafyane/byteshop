package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class OrderMapper {

    public static Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setFullAddress(dto.getFullAddress());
        return address;
    }

    public static Order toEntity(OrderDTO dto) {
        if (dto == null) {
            return null;
        }
        return Order.builder()
                .totalQuantity(dto.getTotalQuantity())
                .totalPrice(dto.getTotalPrice())
                .status(OrderStatus.PENDING)
                .build();
    }

    public static OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) {
            return null;
        }
        OrderItem item = new OrderItem();
        item.setImageUrl(dto.getImageUrl());
        item.setUnitPrice(dto.getUnitPrice());
        item.setQuantity(dto.getQuantity());
        item.setProductId(dto.getProductId());
        return item;
    }

    /**
     * Maps an order to its flat list-view representation. The customer is
     * reduced to their username; no nested entities are copied.
     *
     * <p>Must be called inside a transaction so {@code order.getUser()} can
     * be resolved safely.</p>
     */
    public static OrderSummaryDTO toSummary(Order order) {
        if (order == null) {
            return null;
        }
        return OrderSummaryDTO.builder()
                .id(order.getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .status(order.getStatus())
                .totalQuantity(order.getTotalQuantity())
                .totalPrice(order.getTotalPrice())
                .dateCreated(order.getDateCreated())
                .customerUsername(order.getUser() != null ? order.getUser().getUsername() : null)
                .build();
    }

    /**
     * Maps an order to its full details representation, including the
     * shipping address and the purchased items (with their server-side
     * product name snapshots).
     *
     * <p>Must be called inside a transaction so the lazy {@code orderItems}
     * collection can be materialized.</p>
     */
    public static OrderDetailsDTO toDetails(Order order) {
        if (order == null) {
            return null;
        }
        OrderDetailsDTO details = OrderDetailsDTO.builder()
                .id(order.getId())
                .orderTrackingNumber(order.getOrderTrackingNumber())
                .status(order.getStatus())
                .totalQuantity(order.getTotalQuantity())
                .totalPrice(order.getTotalPrice())
                .dateCreated(order.getDateCreated())
                .customerUsername(order.getUser() != null ? order.getUser().getUsername() : null)
                .build();
        if (order.getAddress() != null) {
            details.setAddress(AddressDTO.builder()
                    .city(order.getAddress().getCity())
                    .fullAddress(order.getAddress().getFullAddress())
                    .build());
        }
        if (order.getOrderItems() != null) {
            details.setOrderItems(order.getOrderItems().stream()
                    .map(item -> OrderItemDTO.builder()
                            .imageUrl(item.getImageUrl())
                            .unitPrice(item.getUnitPrice())
                            .quantity(item.getQuantity())
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .build())
                    .toList());
        }
        return details;
    }
}
