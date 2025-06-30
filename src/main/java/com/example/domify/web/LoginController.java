package com.example.domify.web;

import com.example.domify.model.UserD;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        UserD user = null;

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            user = userService
                    .login(email, password);
            request.getSession().setAttribute("user", user);
            return "redirect:/listings";
        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }


}