package com.mss.exam.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home");
        return "pages/home";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("users", List.of(
                new User("Alice", "alice@example.com", "Admin"),
                new User("Bob", "bob@example.com", "Editor"),
                new User("Carol", "carol@example.com", "Viewer"),
                new User("David", "david@example.com", "Editor"),
                new User("Eve", "eve@example.com", "Admin")
        ));
        return "pages/users";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("pageTitle", "Profile");
        return "pages/profile";
    }

    public record User(String name, String email, String role) {
    }
}
