package com.springboot.recipe_management_system.exceptions;

public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(){
    }

    public RoleNotFoundException(String message){
        super(message);
    }

}
