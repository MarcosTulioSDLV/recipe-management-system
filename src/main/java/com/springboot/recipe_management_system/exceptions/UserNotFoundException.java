package com.springboot.recipe_management_system.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(){
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
