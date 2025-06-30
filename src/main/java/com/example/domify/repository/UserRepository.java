package com.example.domify.repository;

import com.example.domify.model.UserD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserD,Long> {

    Optional<UserD> findByEmail(String email);

    Optional<UserD> findByEmailAndPasswordHash(String email, String password);
}
