package com.example.domify.repository;

import com.example.domify.model.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType,Long> {
    PropertyType findByName (String type);
}
