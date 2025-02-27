package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<Page<RecipeResponseDto>> getAllRecipes(@PageableDefault(size = 15) Pageable pageable){
        return ResponseEntity.ok(recipeService.getAllRecipes(pageable));
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable UUID id){
        return ResponseEntity.ok(recipeService.getRecipeById(id));
    }

    @GetMapping("/by-username-exact-match/{username}/recipes")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByUsername(@PathVariable String username){
        return ResponseEntity.ok(recipeService.getRecipesByUsername(username));
    }

    @GetMapping("/by-username-partial-match/{username}/recipes")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByUsernameContaining(@PathVariable String username){
        return ResponseEntity.ok(recipeService.getRecipesByUsernameContaining(username));
    }

    @GetMapping("/recipes/by-title-exact-match/{title}")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByTitle(@PathVariable String title){
        return ResponseEntity.ok(recipeService.getRecipesByTitle(title));
    }

    @GetMapping("/recipes/by-title-partial-match/{title}")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByTitleContaining(@PathVariable String title){
        return ResponseEntity.ok(recipeService.getRecipesByTitleContaining(title));
    }

    @PostMapping("/users/{userId}/recipes")
    public ResponseEntity<String> addRecipe(@PathVariable UUID userId,
                                            @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.addRecipe(userId,recipeRequestDto);
        return new ResponseEntity<>("Recipe created successfully!",HttpStatus.CREATED);
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable UUID id,
                                               @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.updateRecipe(id,recipeRequestDto);
        return ResponseEntity.ok("Recipe updated successfully!");
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable UUID id){
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok("Recipe with id: "+id+" deleted successfully!");
    }

    //TO DO:
    // POSTMAN AND SECURITY --

}
