package com.example.domify.service;

import com.example.domify.model.TenantProfile;

public interface TenantProfileService {
    TenantProfile findByUserId(Long userId);
}
