package com.example.domify.repository;

import com.example.domify.model.Interested;
import com.example.domify.model.InterestedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestedRepository extends JpaRepository<Interested, InterestedId> {
    @Query("SELECT i FROM Interested i WHERE i.listing.id = :listingId")
    List<Interested> findByListingId(@Param("listingId") Long listingId);

    boolean existsByListingIdAndTenantProfileUserId(Long listingId, Long userId);
}