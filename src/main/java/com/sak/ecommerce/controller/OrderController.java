package com.sak.ecommerce.controller;

import com.sak.ecommerce.model.Cart;
import com.sak.ecommerce.model.Order;
import com.sak.ecommerce.model.User;
import com.sak.ecommerce.service.CartService;
import com.sak.ecommerce.service.OrderService;
import com.sak.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/checkout")
    public String checkoutPage(Model model, Authentication auth) {
        User user = getUser(auth);
        Cart cart = cartService.getCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        model.addAttribute("paymentMethods", Order.PaymentMethod.values());
        model.addAttribute("cartCount", cart.getTotalItems());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String zip,
            @RequestParam(defaultValue = "India") String country,
            @RequestParam String paymentMethod,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            User user = getUser(auth);
            Order.PaymentMethod pm = Order.PaymentMethod.valueOf(paymentMethod);
            Order order = orderService.createOrder(user, address, city, state, zip, country, pm);
            redirectAttributes.addFlashAttribute("orderNumber", order.getOrderNumber());
            redirectAttributes.addFlashAttribute("orderTotal", order.getTotalAmount());
            return "redirect:/orders/" + order.getId() + "?success=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Model model, Authentication auth) {
        User user = getUser(auth);
        model.addAttribute("orders", orderService.getUserOrders(user));
        model.addAttribute("cartCount", getCartCount(user));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, Authentication auth) {
        orderService.getOrderById(id).ifPresent(order -> model.addAttribute("order", order));
        User user = getUser(auth);
        model.addAttribute("cartCount", getCartCount(user));
        return "order-detail";
    }

    private User getUser(Authentication auth) {
        return userService.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private int getCartCount(User user) {
        Cart cart = cartService.getCartByUser(user);
        return cart != null ? cart.getTotalItems() : 0;
    }
}
