package com.springboot.recipe_management_system.exceptions;

public class OwnershipException extends RuntimeException{

    public OwnershipException(){
    }

    public OwnershipException(String message){
        super(message);
    }

}
