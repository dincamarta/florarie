package com.florarie.florarie.controller;

import com.florarie.florarie.repository.FlowerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class   FlowerController {

    private final FlowerRepository flowerRepository;

    public FlowerController(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @GetMapping("/flowers")
    public String list(Model model) {
        model.addAttribute("flowers", flowerRepository.findAll());
        return "flowers/list";
    }
}
