package com.example.domify.web;

import com.example.domify.model.*;
import com.example.domify.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/lease")
public class LeaseController {
    private final LeaseService leaseService;
    private final ListingService listingService;
    private final TenantProfileService tenantProfileService;
    private final LandlordProfileService landlordProfileService;
    private final UserService userService;

    public LeaseController(LeaseService leaseService,
                           ListingService listingService,
                           TenantProfileService tenantProfileService,
                           LandlordProfileService landlordProfileService,
                           UserService userService) {
        this.leaseService = leaseService;
        this.listingService = listingService;
        this.tenantProfileService = tenantProfileService;
        this.landlordProfileService = landlordProfileService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam Long listingId,
                                 @RequestParam Long tenantId,
                                 Model model,
                                 HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null || !userService.isLandlord(user.getId())) {
            return "redirect:/login";
        }

        model.addAttribute("listingId", listingId);
        model.addAttribute("tenantId", tenantId);
        return "create-lease";
    }

    @PostMapping("/create")
    public String createLease(@RequestParam Long listingId,
                              @RequestParam Long tenantId,
                              @RequestParam LocalDate startDate,
                              @RequestParam LocalDate endDate,
                              @RequestParam BigDecimal rentAmount,
                              @RequestParam BigDecimal depositAmount,
                              HttpServletRequest request) {

        try {
            UserD landlordUser = (UserD) request.getSession().getAttribute("user");
            if (landlordUser == null || !userService.isLandlord(landlordUser.getId())) {
                return "redirect:/login";
            }

            Listing listing = listingService.findById(listingId);

            TenantProfile tenant = tenantProfileService.findByUserId(tenantId);
            System.out.println("TENANT: " + tenant.getId());
            System.out.println("LANDLORD: " + landlordUser.getId());
            LandlordProfile landlord = landlordProfileService.findByUserId(landlordUser.getId());

            Lease lease = new Lease(
                    startDate,
                    endDate,
                    rentAmount,
                    depositAmount,
                    listing,
                    tenant,
                    landlord
            );

            leaseService.save(lease);

            listing.setStatus("изнајмено");
            listingService.save(listing);

            return "redirect:/properties/" + listing.getUnit().getProperty().getId();

        } catch (Exception e) {
            return "redirect:/lease/create?listingId=" + listingId + "&tenantId=" + tenantId;
        }
    }

    @GetMapping
    public String getUserLeases(Model model, HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Lease> leases;
        if (userService.isLandlord(user.getId())) {
            leases = leaseService.findByLandlord(user.getId());
        } else {
            leases = leaseService.findByTenant(user.getId());
        }

        model.addAttribute("user", user);
        model.addAttribute("leases", leases);
        return "leases";
    }

    @GetMapping("/{id}/details")
    public String getLeaseDetails(@PathVariable Long id,
                                  Model model,
                                  HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Lease lease = leaseService.findById(id);

        model.addAttribute("lease", lease);
        model.addAttribute("user", user);
        return "lease-details";
    }

    @PostMapping("/{id}/rate-landlord")
    @ResponseBody
    public String rateLandlord(@PathVariable Long id,
                               @RequestParam Integer rating,
                               HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null) {
            return "{\"status\":\"error\",\"message\":\"Not authenticated\"}";
        }

        try {
            // Tenant rates landlord
            leaseService.rateUser(id, user.getId(), rating, false);
            return "{\"status\":\"success\",\"message\":\"Landlord rating saved\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/{id}/rate-tenant")
    @ResponseBody
    public String rateTenant(@PathVariable Long id,
                             @RequestParam Integer rating,
                             HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user == null) {
            return "{\"status\":\"error\",\"message\":\"Not authenticated\"}";
        }

        try {
            // Landlord rates tenant
            leaseService.rateUser(id, user.getId(), rating, true);
            return "{\"status\":\"success\",\"message\":\"Tenant rating saved\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
