package com.example.domify.repository;


import com.example.domify.model.LandlordProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandlordProfileRepository extends JpaRepository<LandlordProfile,Long> {
}
