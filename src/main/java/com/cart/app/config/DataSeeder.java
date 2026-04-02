package com.cart.app.config;

import com.cart.app.model.Product;
import com.cart.app.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.saveAll(Arrays.asList(
                new Product("Wireless Headphones", "Premium noise-cancelling headphones", 2999.99, "https://via.placeholder.com/200x200?text=Headphones", 50, "Electronics"),
                new Product("Mechanical Keyboard", "RGB backlit mechanical keyboard", 1599.99, "https://via.placeholder.com/200x200?text=Keyboard", 30, "Electronics"),
                new Product("Running Shoes", "Lightweight running shoes for all terrains", 1299.00, "https://via.placeholder.com/200x200?text=Shoes", 100, "Footwear"),
                new Product("Yoga Mat", "Anti-slip premium yoga mat", 499.00, "https://via.placeholder.com/200x200?text=Yoga+Mat", 75, "Fitness"),
                new Product("Coffee Maker", "12-cup programmable coffee maker", 2199.00, "https://via.placeholder.com/200x200?text=Coffee+Maker", 20, "Kitchen"),
                new Product("Backpack", "Waterproof travel backpack 45L", 899.00, "https://via.placeholder.com/200x200?text=Backpack", 60, "Bags"),
                new Product("Smart Watch", "Fitness tracking smartwatch", 3999.00, "https://via.placeholder.com/200x200?text=Smart+Watch", 25, "Electronics"),
                new Product("Sunglasses", "UV400 polarized sunglasses", 699.00, "https://via.placeholder.com/200x200?text=Sunglasses", 80, "Accessories")
            ));
            System.out.println("✅ Sample products seeded successfully!");
        }
    }
}
