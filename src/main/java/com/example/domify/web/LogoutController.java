package com.example.domify.web;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {
    @GetMapping
    public String logoutPage(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }

}
