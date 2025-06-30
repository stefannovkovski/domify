package com.example.domify.service.impl;

import com.example.domify.model.Interested;
import com.example.domify.repository.InterestedRepository;
import com.example.domify.service.InterestedService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestedServiceImpl implements InterestedService {
    private final InterestedRepository interestedRepository;

    public InterestedServiceImpl(InterestedRepository interestedRepository) {
        this.interestedRepository = interestedRepository;
    }

    @Override
    public List<Interested> findByListingId(Long id) {
        return interestedRepository.findByListingId(id);
    }

    public Interested save(Interested interested) {
        return interestedRepository.save(interested);
    }

    public boolean existsByListingAndTenant(Long listingId, Long userId) {
        return interestedRepository.existsByListingIdAndTenantProfileUserId(listingId, userId);
    }
}
