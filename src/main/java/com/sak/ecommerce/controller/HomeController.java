package com.sak.ecommerce.controller;

import com.sak.ecommerce.model.Cart;
import com.sak.ecommerce.model.Product;
import com.sak.ecommerce.model.User;
import com.sak.ecommerce.service.CartService;
import com.sak.ecommerce.service.ProductService;
import com.sak.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        List<Product> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("categories", productService.getAllCategories());
        addCartCount(model, auth);
        return "index";
    }

    @GetMapping("/products")
    public String products(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            Model model, Authentication auth) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Product> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId, pageable);
            productService.getCategoryById(categoryId).ifPresent(c -> model.addAttribute("selectedCategory", c));
        } else {
            products = productService.getAllProducts(pageable);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        addCartCount(model, auth);
        return "products";
    }

    @GetMapping("/category/{id}")
    public String category(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            Model model, Authentication auth) {
        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> products = productService.getProductsByCategory(id, pageable);
        productService.getCategoryById(id).ifPresent(c -> model.addAttribute("category", c));
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        addCartCount(model, auth);
        return "products";
    }

    private void addCartCount(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            userService.findByEmail(auth.getName()).ifPresent(user -> {
                Cart cart = cartService.getCartByUser(user);
                model.addAttribute("cartCount", cart != null ? cart.getTotalItems() : 0);
            });
        } else {
            model.addAttribute("cartCount", 0);
        }
    }
}
