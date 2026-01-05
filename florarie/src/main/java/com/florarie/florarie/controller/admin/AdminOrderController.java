package com.florarie.florarie.controller.admin;

import com.florarie.florarie.model.CustomerOrder;
import com.florarie.florarie.model.OrderStatus;
import com.florarie.florarie.model.OrderStep;
import com.florarie.florarie.repository.OrderRepository;
import com.florarie.florarie.repository.OrderStepRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderRepository orderRepository;
    private final OrderStepRepository stepRepository;

    public AdminOrderController(OrderRepository orderRepository, OrderStepRepository stepRepository) {
        this.orderRepository = orderRepository;
        this.stepRepository = stepRepository;
    }


    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders",
                orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda inexistenta: " + id));

        model.addAttribute("steps", stepRepository.findByOrderIdOrderBySortOrderAsc(id));

        model.addAttribute("order", order);
        model.addAttribute("allStatuses", OrderStatus.values());
        return "admin/orders/details";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam @NotNull OrderStatus status) {

        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comanda inexistenta: " + id));

        order.setStatus(status);
        orderRepository.save(order);

        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/overdue")
    public String overdue(Model model) {
        model.addAttribute("orders", orderRepository.findOverdue(
                LocalDateTime.now(),
                List.of(OrderStatus.READY, OrderStatus.CANCELED)
        ));
        return "admin/orders/overdue";
    }

    @PostMapping("/{orderId}/steps/{stepId}/toggle")
    public String toggleStep(@PathVariable Long orderId, @PathVariable Long stepId) {
        OrderStep step = stepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Etapa inexistenta: " + stepId));

        step.setDone(!step.isDone());
        stepRepository.save(step);

        return "redirect:/admin/orders/" + orderId;
    }

    @PostMapping("/{id}/steps/init")
    public String initSteps(@PathVariable Long id) {
        CustomerOrder order = orderRepository.findById(id).orElseThrow();

        if (!stepRepository.existsByOrderId(id)) {
            order.addStep(new OrderStep("Pregătire flori", 1));
            order.addStep(new OrderStep("Asamblare buchet", 2));
            order.addStep(new OrderStep("Ambalare", 3));
            orderRepository.save(order);
        }
        return "redirect:/admin/orders/" + id;
    }



}
