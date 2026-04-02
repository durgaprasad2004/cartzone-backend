package com.cart.app.controller;

import com.cart.app.dto.Dtos.*;
import com.cart.app.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long userId, @RequestBody AddToCartRequest req) {
        return ResponseEntity.ok(cartService.addToCart(userId, req));
    }

    @PutMapping("/{userId}/item/{itemId}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long userId, @PathVariable Long itemId, @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, itemId, quantity));
    }

    @DeleteMapping("/{userId}/item/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long userId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, itemId));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartResponse> clearCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
