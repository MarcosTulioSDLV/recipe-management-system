package com.springboot.recipe_management_system.exceptions;

public class RecipeNotFoundException extends RuntimeException{

    public RecipeNotFoundException(){
    }

    public RecipeNotFoundException(String message){
        super(message);
    }

}
