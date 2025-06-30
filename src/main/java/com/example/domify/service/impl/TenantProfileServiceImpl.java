package com.example.domify.service.impl;

import com.example.domify.model.TenantProfile;
import com.example.domify.repository.TenantProfileRepository;
import com.example.domify.service.TenantProfileService;
import org.springframework.stereotype.Service;

@Service
public class TenantProfileServiceImpl implements TenantProfileService {
    private final TenantProfileRepository tenantProfileRepository;

    public TenantProfileServiceImpl(TenantProfileRepository tenantProfileRepository) {
        this.tenantProfileRepository = tenantProfileRepository;
    }

    public TenantProfile findByUserId(Long userId) {
        return tenantProfileRepository.findByUserId(userId).orElseThrow(RuntimeException::new);
    }
}
