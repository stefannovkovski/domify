package com.example.domify.repository;


import com.example.domify.model.LandlordProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandlordProfileRepository extends JpaRepository<LandlordProfile,Long> {
    Optional<LandlordProfile> findByUserId(Long id);
}
