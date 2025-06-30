package com.example.domify.model.exceptions;

public class PropertyIdNotFoundException extends RuntimeException {
    public PropertyIdNotFoundException(){
        super("Property with that Id was not found in the database!");
    }
}
