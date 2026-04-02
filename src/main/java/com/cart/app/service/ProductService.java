package com.cart.app.service;

import com.cart.app.dto.Dtos.*;
import com.cart.app.model.Product;
import com.cart.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest req) {
        Product p = new Product(req.getName(), req.getDescription(), req.getPrice(),
                req.getImageUrl(), req.getStock(), req.getCategory());
        return toResponse(productRepository.save(p));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        return toResponse(productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id)));
    }

    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());
        p.setStock(req.getStock());
        p.setCategory(req.getCategory());
        return toResponse(productRepository.save(p));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setDescription(p.getDescription());
        r.setPrice(p.getPrice());
        r.setImageUrl(p.getImageUrl());
        r.setStock(p.getStock());
        r.setCategory(p.getCategory());
        return r;
    }
}
