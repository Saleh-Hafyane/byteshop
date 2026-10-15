// Checkout endpoint: turns the cart into a real order for the logged-in user.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.PurchaseRequest;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import jakarta.validation.Valid;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Requires authentication — the user comes from the JWT, never from the request body.
@CrossOrigin
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    // POST /api/checkout/purchase — validates the payload, saves the order, returns its tracking number.
    @PostMapping("/purchase")
    public PurchaseResponse makeOrder(@Valid @RequestBody PurchaseRequest purchase, @AuthenticationPrincipal User user) {
        return checkoutService.makeOrder(purchase, user);
    }
}
