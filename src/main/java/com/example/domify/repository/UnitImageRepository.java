package com.example.domify.repository;

import com.example.domify.model.UnitImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitImageRepository extends JpaRepository<UnitImage, Long> {
}
