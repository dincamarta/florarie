package com.florarie.florarie.controller.admin;

import com.florarie.florarie.model.Flower;
import com.florarie.florarie.repository.FlowerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/flowers")
public class AdminFlowerController {

    private final FlowerRepository flowerRepository;

    public AdminFlowerController(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("flowers", flowerRepository.findAll());
        return "admin/flowers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("flower", new Flower());
        model.addAttribute("mode", "create");
        return "admin/flowers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("flower") Flower flower,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/flowers/form";
        }
        flowerRepository.save(flower);
        return "redirect:/admin/flowers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + id));

        model.addAttribute("flower", flower);
        model.addAttribute("mode", "edit");
        return "admin/flowers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("flower") Flower flower,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/flowers/form";
        }
        flower.setId(id);
        flowerRepository.save(flower);
        return "redirect:/admin/flowers";
    }

    // confirmare ștergere
    @GetMapping("/{id}/delete")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + id));
        model.addAttribute("flower", flower);
        return "admin/flowers/confirm-delete";
    }

    // ștergere efectivă
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        flowerRepository.deleteById(id);
        return "redirect:/admin/flowers";
    }
}
