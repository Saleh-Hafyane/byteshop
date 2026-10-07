package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
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
                .status("PENDING")
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
}
