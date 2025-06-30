package com.example.domify.service.impl;

import com.example.domify.model.LandlordProfile;
import com.example.domify.repository.LandlordProfileRepository;
import com.example.domify.service.LandlordProfileService;
import org.springframework.stereotype.Service;

@Service
public class LandlordProfileServiceImpl implements LandlordProfileService {
    private final LandlordProfileRepository landlordProfileRepository;

    public LandlordProfileServiceImpl(LandlordProfileRepository landlordProfileRepository) {
        this.landlordProfileRepository = landlordProfileRepository;
    }

    @Override
    public LandlordProfile findByUserId(Long userId) {
        return landlordProfileRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
    }
}
