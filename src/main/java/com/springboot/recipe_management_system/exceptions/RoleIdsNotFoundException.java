package com.springboot.recipe_management_system.exceptions;

public class RoleIdsNotFoundException extends RuntimeException{

    public RoleIdsNotFoundException(){
    }

    public RoleIdsNotFoundException(String message){
        super(message);
    }

}
