package com.example.domify.web;


import com.example.domify.model.UserD;
import com.example.domify.service.UnitService;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/units")
public class UnitController {
    private final UserService userService;
    private final UnitService unitService;

    public UnitController(UserService userService, UnitService unitService) {
        this.userService = userService;
        this.unitService = unitService;
    }

    @GetMapping("/{unitId}/details")
    public String getUnitDetails(HttpServletRequest request, @PathVariable Long unitId, Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isLandlord", userService.isLandlord(user.getId()));
        } else {
            model.addAttribute("user", null);
            model.addAttribute("isLandlord", false);
        }
        model.addAttribute("unit", unitService.findDetails(unitId).get());
        return "unit";
    }
}
