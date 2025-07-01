package com.example.domify.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/services")
public class ServicesController {
    @GetMapping
    public String getServices() {
        return "services";
    }

    @GetMapping("/details")
    public String getServicesDetails() {
        return "service-details";
    }

}
