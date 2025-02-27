package com.springboot.recipe_management_system.exceptions;

public class UserEmailExistsException extends RuntimeException{

    public UserEmailExistsException(){
    }

    public UserEmailExistsException(String message){
        super(message);
    }

}
