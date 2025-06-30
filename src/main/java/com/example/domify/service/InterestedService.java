package com.example.domify.service;

import com.example.domify.model.Interested;

import java.util.List;

public interface InterestedService {
    List<Interested> findByListingId(Long id);
    Interested save(Interested interested);
    boolean existsByListingAndTenant(Long listingId, Long userId);

}
