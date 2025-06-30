package com.example.domify.model.exceptions;

public class UserWithEmailAlreadyExists extends RuntimeException {

    public UserWithEmailAlreadyExists(String email) {
        super(String.format("User with the email %s already exists!", email));
    }
}
