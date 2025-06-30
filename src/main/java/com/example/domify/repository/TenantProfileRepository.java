package com.example.domify.repository;

import com.example.domify.model.TenantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantProfileRepository extends JpaRepository<TenantProfile,Long> {
    Optional<TenantProfile> findByUserId(Long userId);
}
