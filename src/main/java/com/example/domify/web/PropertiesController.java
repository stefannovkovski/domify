package com.example.domify.web;

import com.example.domify.model.Property;
import com.example.domify.model.UserD;
import com.example.domify.repository.PropertyTypeRepository;
import com.example.domify.service.PropertiesService;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/properties")
public class PropertiesController {
    private final PropertiesService propertiesService;
    private final PropertyTypeRepository propertyRepository;

    public PropertiesController(PropertiesService propertiesService, PropertyTypeRepository propertyRepository) {
        this.propertiesService = propertiesService;
        this.propertyRepository = propertyRepository;
    }

    @GetMapping("/{userId}")
    public String getProperties(@PathVariable Long userId, Model model) {
        model.addAttribute("properties", propertiesService.findByOwnerId(userId));
        return "properties";
    }

    @GetMapping("/{propertyId}/details")
    public String getPropertiesDetails(@PathVariable Long propertyId, Model model) {
        model.addAttribute("property", propertiesService.findDetails(propertyId).get());
        return "property";
    }

    @GetMapping("/add")
    public String getAddPage( Model model) {
        model.addAttribute("propertyTypes", propertyRepository.findAll());
        return "create-property";
    }

    @PostMapping
    public String addProperty(
            @RequestParam("propertyType") Long propertyTypeId,
            @RequestParam("name") String name,
            @RequestParam(value = "description", defaultValue = "") String description,
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

            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Името на имотот е задолжително.");
                return "redirect:/properties/add";
            }

            if (street == null || street.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Улицата е задолжителна.");
                return "redirect:/properties/add";
            }

            if (municipality == null || municipality.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Општината е задолжителна.");
                return "redirect:/properties/add";
            }

            if (city == null || city.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Градот е задолжителен.");
                return "redirect:/properties/add";
            }

            Property property = propertiesService.createPropertyWithImages(
                    propertyTypeId,
                    name.trim(),
                    description != null ? description.trim() : "",
                    street.trim(),
                    streetNumber != null ? streetNumber.trim() : "",
                    municipality.trim(),
                    city.trim(),
                    country.trim(),
                    images,
                    currentUser
            );

            redirectAttributes.addFlashAttribute("success", "Имотот е успешно додаден!");
            return "redirect:/properties/" + currentUser.getId();

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/properties/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Грешка при додавање на имотот: " + e.getMessage());
            return "redirect:/properties/add";
        }
    }
}