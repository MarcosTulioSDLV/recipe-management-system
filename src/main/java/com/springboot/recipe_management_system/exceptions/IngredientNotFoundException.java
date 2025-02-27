package com.springboot.recipe_management_system.exceptions;

public class IngredientNotFoundException extends RuntimeException{

    public IngredientNotFoundException(){
    }

    public IngredientNotFoundException(String message){
        super(message);
    }

}
