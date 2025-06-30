package com.example.domify.service.impl;

import com.example.domify.model.Listing;
import com.example.domify.repository.ListingRepository;
import com.example.domify.service.ListingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;

    public ListingServiceImpl(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    @Override
    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    @Override
    public Listing save(Listing listing) {
        return listingRepository.save(listing);
    }

    @Override
    public Listing findById(Long id) {
        return listingRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
