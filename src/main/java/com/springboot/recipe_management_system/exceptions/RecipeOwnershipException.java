package com.springboot.recipe_management_system.exceptions;

public class RecipeOwnershipException extends RuntimeException {

    public RecipeOwnershipException(){
    }

    public RecipeOwnershipException(String message){
        super(message);
    }

}
