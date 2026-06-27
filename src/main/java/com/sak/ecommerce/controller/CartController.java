package com.sak.ecommerce.controller;

import com.sak.ecommerce.model.Cart;
import com.sak.ecommerce.model.User;
import com.sak.ecommerce.service.CartService;
import com.sak.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String viewCart(Model model, Authentication auth) {
        User user = getUser(auth);
        Cart cart = cartService.getOrCreateCart(user);
        model.addAttribute("cart", cart);
        model.addAttribute("cartCount", cart.getTotalItems());
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {
        User user = getUser(auth);
        cartService.addToCart(user, productId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart!");
        return "redirect:/products/" + productId;
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long cartItemId,
                             @RequestParam int quantity,
                             Authentication auth) {
        User user = getUser(auth);
        cartService.updateCartItem(user, cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId, Authentication auth) {
        User user = getUser(auth);
        cartService.removeFromCart(user, cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Authentication auth) {
        User user = getUser(auth);
        cartService.clearCart(user);
        return "redirect:/cart";
    }

    private User getUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
