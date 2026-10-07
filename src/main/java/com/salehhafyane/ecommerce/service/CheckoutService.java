package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.PurchaseRequest;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.User;

public interface CheckoutService {
    PurchaseResponse makeOrder(PurchaseRequest purchase, User user);
}
