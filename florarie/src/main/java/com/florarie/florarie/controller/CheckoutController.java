package com.florarie.florarie.controller;

import com.florarie.florarie.dto.BouquetItem;
import com.florarie.florarie.dto.CheckoutForm;
import com.florarie.florarie.model.*;
import com.florarie.florarie.repository.FlowerRepository;
import com.florarie.florarie.repository.OrderRepository;
import com.florarie.florarie.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CheckoutController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FlowerRepository flowerRepository;

    public CheckoutController(OrderRepository orderRepository, UserRepository userRepository, FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.flowerRepository = flowerRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, BouquetItem> getCart(HttpSession session) {
        Object obj = session.getAttribute("bouquet");
        if (obj == null) {
            Map<Long, BouquetItem> cart = new LinkedHashMap<>();
            session.setAttribute("bouquet", cart);
            return cart;
        }
        return (Map<Long, BouquetItem>) obj;
    }

    @GetMapping("/checkout")
    public String checkoutForm(HttpSession session, Model model) {
        Map<Long, BouquetItem> cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/bouquet";
        }
        model.addAttribute("form", new CheckoutForm());
        return "checkout/form";
    }

    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("form") CheckoutForm form,
                             BindingResult bindingResult,
                             HttpSession session,
                             Authentication authentication,
                             Model model) {

        Map<Long, BouquetItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/bouquet";

        if (bindingResult.hasErrors()) {
            return "checkout/form";
        }

        // user autenticat (recomand)
        AppUser user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found in DB: " + authentication.getName()));

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setDeliveryAddress(form.getDeliveryAddress());
        order.setPhone(form.getPhone());
        order.setNotes(form.getNotes());
        order.setStatus(OrderStatus.NEW);

        double total = 0.0;

        // validare stoc + snapshot
        for (BouquetItem it : cart.values()) {
            Flower flower = flowerRepository.findById(it.getFlowerId())
                    .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + it.getFlowerId()));

            if (!flower.isAvailable() || flower.getStock() < it.getQuantity()) {
                bindingResult.reject("stock", "Stoc insuficient pentru " + flower.getName());
                return "checkout/form";
            }

            OrderItem item = new OrderItem(flower.getId(), flower.getName(), flower.getPrice(), it.getQuantity());
            order.addItem(item);
            total += item.getLineTotal();

            // scade stoc
            flower.setStock(flower.getStock() - it.getQuantity());
            flowerRepository.save(flower);
        }

        order.setTotal(total);
        CustomerOrder saved = orderRepository.save(order);

        // golește coșul
        cart.clear();

        return "redirect:/orders/" + saved.getId();
    }
}
