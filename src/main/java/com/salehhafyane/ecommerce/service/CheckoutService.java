package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.Purchase;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.User;

public interface CheckoutService {
    PurchaseResponse makeOrder(Purchase purchase, User user);
}
