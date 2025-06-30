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
import java.util.Map;

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
                                 Model model
                                 ) {
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

            return "redirect:/lease";

        } catch (Exception e) {
            return "redirect:/lease/create?listingId=" + listingId + "&tenantId=" + tenantId;
        }
    }

    @GetMapping
    public String getUserLeases(Model model, HttpServletRequest request) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        List<Lease> leases;
        if (userService.isLandlord(user.getId())) {
            leases = leaseService.findByLandlord(user.getId());
        } else {
            leases = leaseService.findByTenant(user.getId());
        }

        model.addAttribute("isLandlord",userService.isLandlord(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("leases", leases);
        return "leases";
    }

    @GetMapping("/{id}/details")
    public String getLeaseDetails(@PathVariable Long id,
                                  Model model
                                  ) {
        Lease lease = leaseService.findById(id);
        model.addAttribute("lease", lease);
        return "lease";
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

    @PostMapping("/api/leases/{id}/rate")
    @ResponseBody
    public String rateUser(@PathVariable Long id,
                           @RequestBody Map<String, Object> request,
                           HttpServletRequest httpRequest) {
        UserD user = (UserD) httpRequest.getSession().getAttribute("user");
        if (user == null) {
            return "{\"status\":\"error\",\"message\":\"Not authenticated\"}";
        }

        try {
            Integer rating = (Integer) request.get("rating");
            if (rating == null || rating < 1 || rating > 5) {
                return "{\"status\":\"error\",\"message\":\"Invalid rating\"}";
            }

            Lease lease = leaseService.findById(id);

            boolean isLandlord = lease.getLandlord().getUser().getId().equals(user.getId());
            boolean isTenant = lease.getTenant().getUser().getId().equals(user.getId());

            if (!isLandlord && !isTenant) {
                return "{\"status\":\"error\",\"message\":\"User not part of this lease\"}";
            }

            if (isLandlord) {
                leaseService.rateUser(id, lease.getLandlord().getId(), rating, true);
            } else {
                leaseService.rateUser(id, lease.getTenant().getId(), rating, false);
            }

            return "{\"status\":\"success\",\"message\":\"Rating saved successfully\"}";
        } catch (Exception e) {
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
