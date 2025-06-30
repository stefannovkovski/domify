package com.example.domify.service;

import com.example.domify.model.LandlordProfile;

public interface LandlordProfileService {
    LandlordProfile findByUserId(Long userId);
}
