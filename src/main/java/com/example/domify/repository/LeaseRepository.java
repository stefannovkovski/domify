package com.example.domify.repository;

import com.example.domify.model.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {
    @Query("SELECT l FROM Lease l WHERE l.landlord.id = :landlordId")
    List<Lease> findByLandlordId(Long landlordId);

    @Query("SELECT l FROM Lease l WHERE l.tenant.id = :tenantId")
    List<Lease> findByTenantId(Long tenantId);
}
