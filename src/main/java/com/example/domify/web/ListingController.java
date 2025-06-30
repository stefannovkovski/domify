package com.example.domify.web;

import com.example.domify.model.*;
import com.example.domify.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = {"/", "/listings"})
public class ListingController {
    private final ListingService listingService;
    private final UserService userService;
    private final UnitService unitService;
    private final InterestedService interestedService;
    private final TenantProfileService tenantProfileService;

    public ListingController(ListingService listingService, UserService userService, UnitService unitService, InterestedService interestedService, TenantProfileService tenantProfileService) {
        this.listingService = listingService;
        this.userService = userService;
        this.unitService = unitService;
        this.interestedService = interestedService;
        this.tenantProfileService = tenantProfileService;
    }

    @GetMapping
    public String getListings(HttpServletRequest request,Model model) {
        model.addAttribute("listings",listingService.findAll());
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam Long unitId, Model model, HttpServletRequest request) {
        model.addAttribute("unitId", unitId);
        return "create-listing";
    }

    @PostMapping("/save")
    public String saveListing(
            @RequestParam String title,
            @RequestParam String status,
            @RequestParam LocalDate availableFrom,
            @RequestParam LocalDate availableTo,
            @RequestParam(required = false) String description,
            @RequestParam Long unitId,
            HttpServletRequest request) {

        try {
            UserD user = (UserD) request.getSession().getAttribute("user");
            if (user == null || !userService.isLandlord(user.getId())) {
                return "redirect:/listings";
            }

            Unit unit = unitService.findById(unitId);

            Listing listing = new Listing(
                    title,
                    availableFrom,
                    availableTo,
                    status,
                    description,
                    unit
            );

            listingService.save(listing);

            return "redirect:/listings";

        } catch (Exception e) {
            return "redirect:/listings/create?unitId=" + unitId;
        }
    }

    @GetMapping("/listings/{id}")
    public String getListingDetails(@PathVariable Long id,
                                    Model model,
                                    HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null)
            return "redirect:/login";
        boolean isLandlord = userService.isLandlord(user.getId());
        Listing listing = listingService.findById(id);
        model.addAttribute("listing", listing);
        model.addAttribute("isLandlord", isLandlord);
        return "listing";
    }

    @GetMapping("/{id}/applications")
    public String getInterestedTenants(@PathVariable Long id, Model model, HttpServletRequest request) {
        Listing listing = listingService.findById(id);

        List<Interested> interestedList = interestedService.findByListingId(id);

        model.addAttribute("listing", listing);
        model.addAttribute("interestedList", interestedList);
        return "interested";
    }

    @GetMapping("/{id}/apply")
    public String submitApplication(@PathVariable Long id,
                                    HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Listing listing = listingService.findById(id);

            TenantProfile tenantProfile = tenantProfileService.findByUserId(user.getId());

            if (interestedService.existsByListingAndTenant(id, user.getId())) {
                return "redirect:/listings/" + id;
            }

            Interested application = new Interested(listing, tenantProfile);
            interestedService.save(application);

            return "redirect:/listings/" + id;

        } catch (Exception e) {
            return "redirect:/listings/" + id + "/apply";
        }
    }

}
