// Converts order DTOs to entities on the way in, and entities to flat DTOs on the way out.
package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.NoArgsConstructor;

// Static utility class only, so the constructor is private.
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class OrderMapper {

    // Request DTO to a new Address entity, null-safe.
    public static Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setCity(dto.getCity());
        address.setFullAddress(dto.getFullAddress());
        return address;
    }

    // Request DTO to a new Order, always starting as PENDING regardless of what the client sent.
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

    // Request DTO to a new OrderItem; deliberately skips productName so it can't be spoofed.
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

    // Order to the list-row DTO; call inside a transaction so the lazy user can load.
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

    // Order to the full details DTO with address and item lines; call inside a transaction so lazy items can load.
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
        // Address is optional on the DTO, so only set it when the order actually has one.
        if (order.getAddress() != null) {
            details.setAddress(AddressDTO.builder()
                    .city(order.getAddress().getCity())
                    .fullAddress(order.getAddress().getFullAddress())
                    .build());
        }
        // Copies every item, including the server-owned product name snapshot.
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
