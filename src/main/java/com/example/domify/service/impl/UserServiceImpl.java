package com.example.domify.service.impl;

import com.example.domify.model.*;
import com.example.domify.model.exceptions.PasswordsDoNotMatch;
import com.example.domify.model.exceptions.UserWithEmailAlreadyExists;
import com.example.domify.repository.*;
import com.example.domify.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final TenantProfileRepository tenantProfileRepository;
    private final LandlordProfileRepository landlordProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AddressRepository addressRepository,
                           TenantProfileRepository tenantProfileRepository,
                           LandlordProfileRepository landlordProfileRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.tenantProfileRepository = tenantProfileRepository;
        this.landlordProfileRepository = landlordProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserD login(String email, String rawPassword) {
        UserD user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public UserD register(String firstName,
                          String lastName,
                          String email,
                          String password,
                          String repeatedPassword,
                          LocalDate dob,
                          String bio,
                          Address address,
                          String role,
                          boolean isAgent) {

        if (!password.equals(repeatedPassword)) {
            throw new PasswordsDoNotMatch();
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserWithEmailAlreadyExists(email);
        }
        Address savedAddress = addressRepository.save(address);
        String encodedPassword = passwordEncoder.encode(password);
        UserD user = new UserD(firstName, lastName, email, encodedPassword, dob, BigDecimal.ZERO, bio, savedAddress);
        UserD savedUser = userRepository.save(user);
        if (role.equals("tenant")) {
            TenantProfile tenantProfile = new TenantProfile(savedUser);
            tenantProfileRepository.save(tenantProfile);
        } else if (role.equals("landlord")) {
            LandlordProfile landlordProfile = new LandlordProfile(0, isAgent, savedUser);
            landlordProfileRepository.save(landlordProfile);
        } else {
            throw new RuntimeException("Непознат тип на профил.");
        }
        return savedUser;
    }

    @Override
    public Boolean isLandlord(Long userId) {
        return landlordProfileRepository.findById(userId).isPresent();
    }

    @Override
    public UserD findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void updateUserRating(Long userId, BigDecimal newRating) {
        UserD user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Calculate new average rating (you might want more sophisticated logic)
        if (user.getRating().compareTo(BigDecimal.ZERO) == 0) {
            user.setRating(newRating);
        } else {
            BigDecimal average = user.getRating()
                    .add(newRating)
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            user.setRating(average);
        }

        userRepository.save(user);
    }
}
