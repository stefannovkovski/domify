package com.example.domify.service;

import com.example.domify.model.Listing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ListingService {
    List<Listing> findAll();
    Listing save(Listing listing);
    Listing findById(Long id);
}
