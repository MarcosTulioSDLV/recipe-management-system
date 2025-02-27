package com.springboot.recipe_management_system.exceptions;

public class UsernameExistsException extends RuntimeException{

    public UsernameExistsException(){
    }

    public UsernameExistsException(String message){
        super(message);
    }

}
