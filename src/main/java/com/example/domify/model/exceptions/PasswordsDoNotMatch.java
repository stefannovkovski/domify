package com.example.domify.model.exceptions;

public class PasswordsDoNotMatch extends RuntimeException {

    public PasswordsDoNotMatch(){
        super("Passwords do not match!");
    }
}