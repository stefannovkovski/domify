package com.example.domify.service;

import com.example.domify.model.Address;
import com.example.domify.model.UserD;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserService {

    UserD login(String email, String password);
    UserD register(String firstName,
                   String lastName,
                   String email,
                   String password,
                   String repeatedPassword,
                   LocalDate dob,
                   String bio,
                   Address address,
                   String role,
                   boolean isAgent);

    Boolean isLandlord(Long userId);
    UserD findById(Long userId);
    void updateUserRating(Long userId, BigDecimal newRating);
}
