package com.example.domify.web;

import com.example.domify.model.UserD;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttributes(HttpServletRequest request, Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        model.addAttribute("user", user);

        if (user != null) {
            boolean isLandlord = userService.isLandlord(user.getId());
            model.addAttribute("isLandlord", isLandlord);
        } else {
            model.addAttribute("isLandlord", false);
        }
    }
}
