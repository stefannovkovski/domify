package com.example.domify.web;

import com.example.domify.model.Property;
import com.example.domify.model.UserD;
import com.example.domify.repository.PropertyTypeRepository;
import com.example.domify.service.PropertiesService;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/properties")
public class PropertiesController {
    private final PropertiesService propertiesService;
    private final UserService userService;
    private final PropertyTypeRepository propertyRepository;

    public PropertiesController(PropertiesService propertiesService, UserService userService, PropertyTypeRepository propertyRepository) {
        this.propertiesService = propertiesService;
        this.userService = userService;
        this.propertyRepository = propertyRepository;
    }

    @GetMapping("/{userId}")
    public String getProperties(HttpServletRequest request, @PathVariable Long userId, Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isLandlord", userService.isLandlord(user.getId()));
        } else {
            model.addAttribute("user", null);
            model.addAttribute("isLandlord", false);
        }
        model.addAttribute("properties", propertiesService.findByOwnerId(userId));
        return "properties";
    }

    @GetMapping("/{propertyId}/details")
    public String getPropertiesDetails(HttpServletRequest request, @PathVariable Long propertyId, Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isLandlord", userService.isLandlord(user.getId()));
        } else {
            model.addAttribute("user", null);
            model.addAttribute("isLandlord", false);
        }
        model.addAttribute("property", propertiesService.findDetails(propertyId).get());
        return "property";
    }

    @GetMapping("/add")
    public String getAddPage(HttpServletRequest request, Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isLandlord", userService.isLandlord(user.getId()));
        } else {
            model.addAttribute("user", null);
            model.addAttribute("isLandlord", false);
        }
        model.addAttribute("propertyTypes", propertyRepository.findAll());
        return "create-property";
    }

    @PostMapping
    public String addProperty(
            @RequestParam("propertyType") Long propertyTypeId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("street") String street,
            @RequestParam(value = "streetNumber", defaultValue = "") String streetNumber,
            @RequestParam("municipality") String municipality,
            @RequestParam("city") String city,
            @RequestParam("country") String country,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        try {
            UserD currentUser = (UserD) request.getSession().getAttribute("user");
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "Мора да бидете најавени за да додадете имот.");
                return "redirect:/login";
            }

            Property property = propertiesService.createPropertyWithImages(
                    propertyTypeId,
                    name,
                    description,
                    street,
                    streetNumber,
                    municipality,
                    city,
                    country,
                    images,
                    currentUser
            );

            redirectAttributes.addFlashAttribute("success", "Имотот е успешно додаден!");
            return "redirect:/properties/" + currentUser.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка при додавање на имотот: " + e.getMessage());
            return "redirect:/properties/add";
        }
    }
}