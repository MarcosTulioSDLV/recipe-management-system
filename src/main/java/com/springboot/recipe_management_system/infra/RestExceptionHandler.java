package com.springboot.recipe_management_system.infra;

import com.springboot.recipe_management_system.exceptions.*;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    //Role class
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException e){
        return handleCustomException(e,HttpStatus.NOT_FOUND);
    }

    //-----
    //User class

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e){
        return handleCustomException(e,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<Object> handleUsernameExistsException(UsernameExistsException e){
        return handleCustomException(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserEmailExistsException.class)
    public ResponseEntity<Object> handleUserEmailExistsException(UserEmailExistsException e){
        return handleCustomException(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleIdsNotFoundException.class)
    public ResponseEntity<Object> handleRoleIdsNotFoundException(RoleIdsNotFoundException e){
        return handleCustomException(e,HttpStatus.NOT_FOUND);
    }
    //-----
    //Recipe

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Object> handleRecipeNotFoundException(RecipeNotFoundException e){
        return handleCustomException(e,HttpStatus.NOT_FOUND);
    }
    //-----
    // Ingredient

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<Object> handleIngredientNotFoundException(IngredientNotFoundException e){
        return handleCustomException(e,HttpStatus.NOT_FOUND);
    }

    //-----
    private ResponseEntity<Object> handleCustomException(RuntimeException e,HttpStatus httpStatus) {
        Map<String,Object> responseMessage= new LinkedHashMap<>();
        responseMessage.put("message",e.getMessage());
        responseMessage.put("status",httpStatus.value());
        return new ResponseEntity<>(responseMessage, httpStatus);
    }

}
