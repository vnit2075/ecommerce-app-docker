package com.sak.ecommerce.config;

import com.sak.ecommerce.model.Category;
import com.sak.ecommerce.model.Product;
import com.sak.ecommerce.model.User;
import com.sak.ecommerce.repository.CategoryRepository;
import com.sak.ecommerce.repository.ProductRepository;
import com.sak.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() > 0) {
            log.info("Database already seeded, skipping initialization.");
            return;
        }
        log.info("Seeding database with sample data...");
        seedUsers();
        seedCategories();
        log.info("Database seeded successfully!");
    }

    private void seedUsers() {
        // Admin user
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setEmail("admin@shopez.com");
        admin.setPassword("admin123");
        admin.setPhone("9999999999");
        admin.setRole(User.Role.ADMIN);
        try { userService.registerUser(admin); } catch (Exception ignored) {}

        // Customer user
        User customer = new User();
        customer.setFullName("John Doe");
        customer.setEmail("john@example.com");
        customer.setPassword("john1234");
        customer.setPhone("8888888888");
        try { userService.registerUser(customer); } catch (Exception ignored) {}

        log.info("Users seeded: admin@shopez.com (admin123), john@example.com (john1234)");
    }

    private void seedCategories() {
        // Categories
        Category electronics = createCategory("Electronics", "Gadgets, phones, laptops and more",
                "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400");
        Category fashion = createCategory("Fashion", "Clothing, shoes and accessories",
                "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400");
        Category home = createCategory("Home & Living", "Furniture, decor and appliances",
                "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400");
        Category books = createCategory("Books", "Books, e-books and audiobooks",
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400");
        Category sports = createCategory("Sports & Fitness", "Equipment, apparel and nutrition",
                "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400");
        Category beauty = createCategory("Beauty & Health", "Skincare, haircare and wellness",
                "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=400");

        // Electronics Products
        createProduct("Apple iPhone 15 Pro", "Latest iPhone with titanium design, A17 Pro chip, 48MP camera system",
                new BigDecimal("129999"), new BigDecimal("119999"), 50, electronics, "Apple",
                "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400", true);
        createProduct("Samsung Galaxy S24 Ultra", "Samsung flagship with 200MP camera and built-in S Pen",
                new BigDecimal("109999"), new BigDecimal("99999"), 35, electronics, "Samsung",
                "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400", true);
        createProduct("Sony WH-1000XM5 Headphones", "Industry leading noise canceling wireless headphones",
                new BigDecimal("29990"), new BigDecimal("24990"), 100, electronics, "Sony",
                "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", true);
        createProduct("MacBook Air M3", "Apple MacBook Air with M3 chip, 13-inch display",
                new BigDecimal("114900"), new BigDecimal("109900"), 25, electronics, "Apple",
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", false);
        createProduct("iPad Pro 12.9", "Apple iPad Pro with M2 chip and Liquid Retina XDR display",
                new BigDecimal("89900"), null, 40, electronics, "Apple",
                "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400", false);

        // Fashion Products
        createProduct("Men's Classic Fit Jeans", "Premium denim jeans with classic straight fit",
                new BigDecimal("2999"), new BigDecimal("1999"), 200, fashion, "Levi's",
                "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400", true);
        createProduct("Women's Floral Maxi Dress", "Elegant floral print maxi dress perfect for any occasion",
                new BigDecimal("3499"), new BigDecimal("2499"), 150, fashion, "Zara",
                "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=400", false);
        createProduct("Nike Air Max 270", "Comfortable lifestyle sneakers with Max Air unit",
                new BigDecimal("12995"), new BigDecimal("10995"), 80, fashion, "Nike",
                "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", true);
        createProduct("Leather Wallet", "Genuine leather slim wallet with RFID blocking",
                new BigDecimal("1499"), new BigDecimal("999"), 300, fashion, "Fossil",
                "https://images.unsplash.com/photo-1627123424574-724758594e93?w=400", false);

        // Home & Living Products
        createProduct("Ergonomic Office Chair", "Premium mesh back ergonomic chair with lumbar support",
                new BigDecimal("24999"), new BigDecimal("18999"), 30, home, "Herman Miller",
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400", true);
        createProduct("Instant Pot Duo 7-in-1", "7-in-1 multi-use programmable pressure cooker",
                new BigDecimal("6999"), new BigDecimal("5499"), 60, home, "Instant Pot",
                "https://images.unsplash.com/photo-1585515320310-259814833e62?w=400", false);
        createProduct("Dyson V15 Vacuum", "Cordless vacuum cleaner with laser dust detection",
                new BigDecimal("52900"), new BigDecimal("45900"), 20, home, "Dyson",
                "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", false);

        // Books
        createProduct("Atomic Habits", "An Easy & Proven Way to Build Good Habits & Break Bad Ones",
                new BigDecimal("499"), new BigDecimal("399"), 500, books, "James Clear",
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400", true);
        createProduct("The Lean Startup", "How Today's Entrepreneurs Use Continuous Innovation",
                new BigDecimal("399"), new BigDecimal("319"), 300, books, "Eric Ries",
                "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=400", false);

        // Sports
        createProduct("Yoga Mat Premium", "Extra thick 6mm non-slip yoga mat with carrying strap",
                new BigDecimal("2499"), new BigDecimal("1799"), 200, sports, "Liforme",
                "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400", true);
        createProduct("Adjustable Dumbbells Set", "Adjustable weight dumbbells 5-52.5 lbs",
                new BigDecimal("19999"), new BigDecimal("15999"), 40, sports, "Bowflex",
                "https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?w=400", false);

        // Beauty
        createProduct("Vitamin C Serum", "20% Vitamin C brightening face serum with Hyaluronic acid",
                new BigDecimal("1299"), new BigDecimal("999"), 400, beauty, "TreSemme",
                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=400", true);
        createProduct("Electric Face Massager", "Facial roller massager for lifting and anti-aging",
                new BigDecimal("3499"), new BigDecimal("2299"), 150, beauty, "FOREO",
                "https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=400", false);
    }

    private Category createCategory(String name, String description, String imageUrl) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImageUrl(imageUrl);
        category.setActive(true);
        return categoryRepository.save(category);
    }

    private void createProduct(String name, String description, BigDecimal price, BigDecimal discountPrice,
                                int stock, Category category, String brand, String imageUrl, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setStockQuantity(stock);
        product.setCategory(category);
        product.setBrand(brand);
        product.setImageUrl(imageUrl);
        product.setFeatured(featured);
        product.setActive(true);
        productRepository.save(product);
    }
}
