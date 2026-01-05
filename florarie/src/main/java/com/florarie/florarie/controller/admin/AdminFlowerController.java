/** Controller pentru gestionarea administrarii florilor de catre admin
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.controller.admin;

import com.florarie.florarie.model.Flower;
import com.florarie.florarie.repository.FlowerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/flowers")
public class AdminFlowerController {

    private final FlowerRepository flowerRepository;

    @Value("${upload.path:uploads}")
    private String uploadPath;

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
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "admin/flowers/form";
        }
        
        // salvare imagine dacă există
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                flower.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                model.addAttribute("uploadError", "Eroare la salvarea imaginii: " + e.getMessage());
                model.addAttribute("mode", "create");
                return "admin/flowers/form";
            }
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
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            return "admin/flowers/form";
        }
        
        // păstrează imaginea existentă dacă nu e încărcată una nouă
        Flower existing = flowerRepository.findById(id).orElse(null);
        if (existing != null) {
            flower.setImagePath(existing.getImagePath());
        }
        
        // salvare imagine nouă dacă există
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                flower.setImagePath("/uploads/" + fileName);
            } catch (IOException e) {
                model.addAttribute("uploadError", "Eroare la salvarea imaginii: " + e.getMessage());
                model.addAttribute("mode", "edit");
                return "admin/flowers/form";
            }
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
    
    // metoda helper pentru salvare imagine
    private String saveImage(MultipartFile file) throws IOException {
        // creare director uploads dacă nu există
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        // generare nume unic
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        
        // salvare fișier
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }
}
