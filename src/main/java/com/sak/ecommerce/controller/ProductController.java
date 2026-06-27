package com.sak.ecommerce.controller;

import com.sak.ecommerce.model.Cart;
import com.sak.ecommerce.model.Product;
import com.sak.ecommerce.service.CartService;
import com.sak.ecommerce.service.ProductService;
import com.sak.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model, Authentication auth) {
        productService.getProductById(id).ifPresent(product -> {
            model.addAttribute("product", product);
            // Related products from same category
            if (product.getCategory() != null) {
                var related = productService.getProductsByCategory(
                        product.getCategory().getId(),
                        org.springframework.data.domain.PageRequest.of(0, 4)).getContent();
                model.addAttribute("relatedProducts",
                        related.stream().filter(p -> !p.getId().equals(id)).limit(3).toList());
            }
        });
        model.addAttribute("categories", productService.getAllCategories());
        addCartCount(model, auth);
        return "product-detail";
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
