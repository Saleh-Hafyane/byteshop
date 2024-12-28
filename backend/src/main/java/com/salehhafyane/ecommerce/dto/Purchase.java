package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
import com.salehhafyane.ecommerce.entity.User;
import lombok.Data;


import java.util.Set;
@Data
public class Purchase {
    private Address address;
    private Order order;
    private Set<OrderItem> orderItems;
}
