package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.Purchase;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public PurchaseResponse makeOrder(@RequestBody Purchase purchase, @AuthenticationPrincipal User user) {
        return checkoutService.makeOrder(purchase, user);
    }
}
