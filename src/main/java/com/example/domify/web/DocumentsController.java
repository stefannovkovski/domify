package com.example.domify.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documents")
public class DocumentsController {
    @GetMapping
    public String getDocuments() {
        return "documents";
    }
}
