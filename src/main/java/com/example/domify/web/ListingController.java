package com.example.domify.web;

import com.example.domify.model.UserD;
import com.example.domify.service.AddressService;
import com.example.domify.service.ListingService;
import com.example.domify.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/listings"})
public class ListingController {
    private final AddressService addressService;
    private final ListingService listingService;

    private final UserService userService;

    public ListingController(AddressService addressService, ListingService listingService, UserService userService) {
        this.addressService = addressService;
        this.listingService = listingService;
        this.userService = userService;
    }

    @GetMapping
    public String getListings(HttpServletRequest request,Model model) {
        UserD user = (UserD) request.getSession().getAttribute("user");
        if (user != null)
        {
            model.addAttribute("user",user);
            model.addAttribute("isLandlord",userService.isLandlord(user.getId()));
        }else {
            model.addAttribute("user", null);
            model.addAttribute("isLandlord", false);
        }

        model.addAttribute("listings",listingService.findAll());
        return "index";
    }
}
