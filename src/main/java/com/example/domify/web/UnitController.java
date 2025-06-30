package com.example.domify.web;


import com.example.domify.model.Property;
import com.example.domify.model.Unit;
import com.example.domify.model.UserD;
import com.example.domify.service.PropertiesService;
import com.example.domify.service.UnitService;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
@RequestMapping("/units")
public class UnitController {
    private final UserService userService;
    private final UnitService unitService;
    private final PropertiesService propertiesService;

    public UnitController(UserService userService, UnitService unitService, PropertiesService propertiesService) {
        this.userService = userService;
        this.unitService = unitService;
        this.propertiesService = propertiesService;
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

    @GetMapping("/add")
    public String getAddPage(@RequestParam Long propertyId, Model model) {
        model.addAttribute("propertyId", propertyId);
        return "create-unit";
    }

    @PostMapping("/save")
    public String saveUnitForm(
            @RequestParam String unitNumber,
            @RequestParam Integer floor,
            @RequestParam Integer bedrooms,
            @RequestParam Integer bathrooms,
            @RequestParam Double area,
            @RequestParam Double rent,
            @RequestParam Long propertyId,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        try {
            BigDecimal areaSqM = BigDecimal.valueOf(area);
            BigDecimal rentAmount = BigDecimal.valueOf(rent);

            Property property = propertiesService.findById(propertyId);

            Unit unit = new Unit(unitNumber, floor, bedrooms, bathrooms, areaSqM, rentAmount, property);
            unitService.save(unit, images);
            return "redirect:/properties/" + propertyId + "/details";
        } catch (Exception e) {
            return "redirect:/units/add?propertyId=" + propertyId;
        }
    }
}
