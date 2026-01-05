package com.florarie.florarie.controller;

import com.florarie.florarie.dto.RegisterForm;
import com.florarie.florarie.model.AppUser;
import com.florarie.florarie.model.Role;
import com.florarie.florarie.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterForm form,
                           BindingResult bindingResult,
                           Model model) {

        // 1) parolele trebuie sa fie egale
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "match", "Parolele nu coincid");
        }

        // 2) email unic
        if (userRepository.existsByEmail(form.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Există deja un cont cu acest email");
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // 3) salveaza user cu rol USER + parola criptata
        AppUser user = new AppUser();
        user.setEmail(form.getEmail());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        // dupa register -> login
        return "redirect:/login?registered";
    }

    // optional: pagina de login custom (nu e obligatoriu, poti lasa default Spring)
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
