package com.florarie.florarie.controller;

import com.florarie.florarie.dto.BouquetItem;
import com.florarie.florarie.model.Flower;
import com.florarie.florarie.repository.FlowerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/bouquet")
public class BouquetController {

    private final FlowerRepository flowerRepository;

    public BouquetController(FlowerRepository flowerRepository) {
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

    @GetMapping
    public String view(HttpSession session, Model model) {
        Map<Long, BouquetItem> cart = getCart(session);
        double total = cart.values().stream().mapToDouble(BouquetItem::getLineTotal).sum();

        model.addAttribute("items", cart.values());
        model.addAttribute("total", total);
        return "bouquet/view";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, HttpSession session) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + id));

        Map<Long, BouquetItem> cart = getCart(session);

        BouquetItem item = cart.get(id);
        if (item == null) {
            cart.put(id, new BouquetItem(flower.getId(), flower.getName(), flower.getPrice(), 1));
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }

        return "redirect:/bouquet";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long flowerId, @RequestParam int quantity, HttpSession session) {
        Map<Long, BouquetItem> cart = getCart(session);

        if (quantity <= 0) {
            cart.remove(flowerId);
        } else {
            BouquetItem item = cart.get(flowerId);
            if (item != null) item.setQuantity(quantity);
        }
        return "redirect:/bouquet";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session) {
        Map<Long, BouquetItem> cart = getCart(session);
        cart.remove(id);
        return "redirect:/bouquet";
    }
}
