package com.florarie.florarie.controller;

import com.florarie.florarie.model.AppUser;
import com.florarie.florarie.model.CustomerOrder;
import com.florarie.florarie.repository.OrderRepository;
import com.florarie.florarie.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrdersController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String myOrders(Authentication auth, Model model) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("orders", orderRepository.findByUserOrderByCreatedAtDesc(user));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Authentication auth, Model model) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        CustomerOrder order = orderRepository.findById(id).orElseThrow();

        if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "orders/details";
    }

    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable Long id, Authentication auth, Model model) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        CustomerOrder order = orderRepository.findById(id).orElseThrow();

        if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "orders/confirm-delete";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, Authentication auth) {
        AppUser user = userRepository.findByEmail(auth.getName()).orElseThrow();
        CustomerOrder order = orderRepository.findById(id).orElseThrow();

        if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        orderRepository.delete(order);
        return "redirect:/orders";
    }
}
