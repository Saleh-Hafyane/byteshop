package com.salehhafyane.ecommerce.config;

import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.City;
import com.salehhafyane.ecommerce.repository.UserRepository;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import com.salehhafyane.ecommerce.repository.ProductRepository;
import com.salehhafyane.ecommerce.repository.CityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            UserRepository userRepository,
            ProductCategoryRepository categoryRepository,
            ProductRepository productRepository,
            CityRepository cityRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Create a new user with the role of Admin.
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            // Seed Cities
            if (cityRepository.count() == 0) {
                String[] cityNames = {"New York", "Los Angeles", "Paris", "London", "Casablanca", "Rabat"};
                for (String name : cityNames) {
                    City city = new City();
                    city.setCityName(name);
                    cityRepository.save(city);
                }
            }

            // Seed Product Categories & Products
            if (categoryRepository.count() == 0) {
                ProductCategory laptopsCat = new ProductCategory();
                laptopsCat.setCategoryName("Laptops");
                laptopsCat = categoryRepository.save(laptopsCat);

                ProductCategory gamingLaptopsCat = new ProductCategory();
                gamingLaptopsCat.setCategoryName("Gaming Laptops");
                gamingLaptopsCat = categoryRepository.save(gamingLaptopsCat);

                // Laptops
                createProduct(productRepository, laptopsCat, 
                        "Dell Latitude 5330 G12", 
                        "Dell Latitude 5330 Intel Core i5 12th Gen 13.3-inch Laptop", 
                        new BigDecimal("899.99"), 
                        "images/products/laptops/dell-latitude-5330-g12.jpg", 15);

                createProduct(productRepository, laptopsCat, 
                        "HP EliteBook 845 G7", 
                        "HP EliteBook 845 G7 AMD Ryzen 7 14-inch Laptop", 
                        new BigDecimal("949.99"), 
                        "images/products/laptops/hp-elitebook-845-g7.jpg", 10);

                createProduct(productRepository, laptopsCat, 
                        "Lenovo ThinkPad T14s Gen 3", 
                        "Lenovo ThinkPad T14s Gen 3 Intel Core i7 14-inch Laptop", 
                        new BigDecimal("1199.99"), 
                        "images/products/laptops/lenovo-thinkpad-t14s-gen-3.jpg", 8);

                // Gaming Laptops
                createProduct(productRepository, gamingLaptopsCat, 
                        "ASUS ROG Strix Scar 16", 
                        "ASUS ROG Strix Scar 16 Intel Core i9 GeForce RTX 4080 Gaming Laptop", 
                        new BigDecimal("2499.99"), 
                        "images/products/gaming-laptops/asus-rog-strix-scar-16.jpg", 5);

                createProduct(productRepository, gamingLaptopsCat, 
                        "ASUS TUF Gaming F15", 
                        "ASUS TUF Gaming F15 Intel Core i7 GeForce RTX 4060 Gaming Laptop", 
                        new BigDecimal("1099.99"), 
                        "images/products/gaming-laptops/asus-tuf-f15.jpg", 12);

                createProduct(productRepository, gamingLaptopsCat, 
                        "Lenovo Legion Pro 7", 
                        "Lenovo Legion Pro 7 AMD Ryzen 9 GeForce RTX 4090 Gaming Laptop", 
                        new BigDecimal("2799.99"), 
                        "images/products/gaming-laptops/lenovo-legion-pro-7.jpg", 4);
            }
        };
    }

    private void createProduct(ProductRepository productRepository, ProductCategory category, 
                               String name, String description, BigDecimal price, String imageUrl, int stock) {
        Product product = new Product();
        product.setCategory(category);
        product.setName(name);
        product.setDescription(description);
        product.setUnitPrice(price);
        product.setImageUrl(imageUrl);
        product.setUnitsInStock(stock);
        productRepository.save(product);
    }
}