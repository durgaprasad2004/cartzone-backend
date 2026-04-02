package com.cart.app.service;

import com.cart.app.dto.Dtos.*;
import com.cart.app.model.*;
import com.cart.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       UserRepository userRepository, ProductRepository productRepository,
                       ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    private Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public CartResponse getCartByUserId(Long userId) {
        return toResponse(getOrCreateCart(userId));
    }

    @Transactional
    public CartResponse addToCart(Long userId, AddToCartRequest req) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + req.getProductId()));

        if (product.getStock() < req.getQuantity())
            throw new RuntimeException("Insufficient stock for: " + product.getName());

        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + req.getQuantity());
            cartItemRepository.save(item);
        } else {
            cartItemRepository.save(new CartItem(cart, product, req.getQuantity()));
        }

        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Transactional
    public CartResponse updateCartItem(Long userId, Long itemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + itemId));
        if (!item.getCart().getId().equals(cart.getId()))
            throw new RuntimeException("Item does not belong to user's cart");
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Transactional
    public CartResponse removeFromCart(Long userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + itemId));
        if (!item.getCart().getId().equals(cart.getId()))
            throw new RuntimeException("Item does not belong to user's cart");
        cartItemRepository.delete(item);
        return toResponse(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Transactional
    public CartResponse clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
        return toResponse(cart);
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {
            CartItemResponse ci = new CartItemResponse();
            ci.setId(item.getId());
            ci.setProduct(productService.toResponse(item.getProduct()));
            ci.setQuantity(item.getQuantity());
            ci.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
            return ci;
        }).collect(Collectors.toList());

        double total = itemResponses.stream().mapToDouble(CartItemResponse::getSubtotal).sum();
        int count = itemResponses.stream().mapToInt(CartItemResponse::getQuantity).sum();

        CartResponse r = new CartResponse();
        r.setCartId(cart.getId());
        r.setUserId(cart.getUser().getId());
        r.setUsername(cart.getUser().getUsername());
        r.setItems(itemResponses);
        r.setTotal(total);
        r.setItemCount(count);
        return r;
    }
}
