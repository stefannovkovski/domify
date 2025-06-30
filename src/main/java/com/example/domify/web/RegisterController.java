package com.example.domify.web;

import com.example.domify.model.Address;
import com.example.domify.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String dob,
                           @RequestParam String bio,
                           @RequestParam String street,
                           @RequestParam String number,
                           @RequestParam String municipality,
                           @RequestParam String city,
                           @RequestParam String country,
                           @RequestParam String role,
                           @RequestParam(required = false, defaultValue = "false") boolean is_agent) {

        try {
            Address address = new Address(street, number, municipality, city, country);
            userService.register(
                    firstName,
                    lastName,
                    email,
                    password,
                    repeatedPassword,
                    LocalDate.parse(dob),
                    bio,
                    address,
                    role,
                    is_agent
            );
            return "redirect:/login";
        } catch (RuntimeException ex) {
            return "redirect:/register?error=" + ex.getMessage();
        }
    }
}
