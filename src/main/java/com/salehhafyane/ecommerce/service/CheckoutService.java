// Contract for turning a checkout request into a saved order.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.PurchaseRequest;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.User;

public interface CheckoutService {
    // Saves the order for this user and returns the new tracking number.
    PurchaseResponse makeOrder(PurchaseRequest purchase, User user);
}
