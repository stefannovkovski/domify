package com.example.domify.service.impl;

import com.example.domify.model.Lease;
import com.example.domify.model.UserD;
import com.example.domify.repository.LeaseRepository;
import com.example.domify.service.LeaseService;
import com.example.domify.service.ListingService;
import com.example.domify.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LeaseServiceImpl implements LeaseService {
    private final LeaseRepository leaseRepository;
    private final ListingService listingService;
    private final UserService userService;
    public LeaseServiceImpl(LeaseRepository leaseRepository, ListingService listingService, UserService userService) {
        this.leaseRepository = leaseRepository;
        this.listingService = listingService;
        this.userService = userService;
    }

    @Transactional
    public Lease save(Lease lease) {
        return leaseRepository.save(lease);
    }

    public List<Lease> findByLandlord(Long landlordId) {
        return leaseRepository.findByLandlordId(landlordId);
    }

    public List<Lease> findByTenant(Long tenantId) {
        return leaseRepository.findByTenantId(tenantId);
    }

    @Override
    public Lease findById(Long leaseId) {
        return leaseRepository.findById(leaseId).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void rateUser(Long leaseId, Long raterProfileId, Integer rating, boolean isRatingTenant) {
        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new IllegalArgumentException("Lease not found"));

        boolean isRaterLandlord = lease.getLandlord().getId().equals(raterProfileId);
        boolean isRaterTenant = lease.getTenant().getId().equals(raterProfileId);

        if (!isRaterLandlord && !isRaterTenant) {
            throw new IllegalArgumentException("User is not part of this lease");
        }

        if (isRatingTenant && !isRaterLandlord) {
            throw new IllegalArgumentException("Only landlord can rate tenant");
        }

        if (!isRatingTenant && !isRaterTenant) {
            throw new IllegalArgumentException("Only tenant can rate landlord");
        }

        UserD userToRate = isRatingTenant ? lease.getTenant().getUser() : lease.getLandlord().getUser();

        userService.updateUserRating(userToRate.getId(), BigDecimal.valueOf(rating));
    }
}
