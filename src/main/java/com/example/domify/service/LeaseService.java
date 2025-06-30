package com.example.domify.service;

import com.example.domify.model.LandlordProfile;
import com.example.domify.model.Lease;

import java.util.List;

public interface LeaseService {
    Lease save(Lease lease);
    List<Lease> findByLandlord(Long landlordId);
    List<Lease> findByTenant(Long tenantId);
    Lease findById(Long leaseId);
    void rateUser(Long leaseId, Long raterId, Integer rating, boolean isRatingTenant);
}
