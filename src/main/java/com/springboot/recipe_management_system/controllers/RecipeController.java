package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Recipe Controller", description = "Controller for managing recipes")
@RestController
@RequestMapping(value = "/api/v1")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(
            summary = "Get a recipe by id for the authenticated user",
            description = "Retrieves a recipe by its id, ensuring it belongs to the authenticated user.")
    @GetMapping("/recipes/self/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeByIdForSelf(@PathVariable UUID id){
        return ResponseEntity.ok(recipeService.getRecipeById(id,true));
    }

    @Operation(
            summary = "Get a recipe by id",
            description = "Retrieves a recipe by its id. Only accessible by ADMIN users.")
    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable UUID id){
        return ResponseEntity.ok(recipeService.getRecipeById(id,false));
    }

    @Operation(
            summary = "Get all recipes for the authenticated user",
            description = "Retrieves all recipes belonging to the authenticated user, with optional filtering by title.")
    @GetMapping("/recipes/self")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesForSelf(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String titleContains) {

        if (title != null)
            return ResponseEntity.ok(recipeService.getRecipesByTitleForSelf(title));
        if (titleContains != null)
            return ResponseEntity.ok(recipeService.getRecipesByTitleContainingForSelf(titleContains));

        return ResponseEntity.ok(recipeService.getAllRecipesForSelf());
    }

    @Operation(
            summary = "Get all recipes",
            description = "Retrieves all recipes with optional filtering by title and username. Only accessible by ADMIN users.")
    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeResponseDto>> getRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String titleContains,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String usernameContains) {

        if (title != null)
            return ResponseEntity.ok(recipeService.getRecipesByTitle(title));
        if (titleContains != null)
            return ResponseEntity.ok(recipeService.getRecipesByTitleContaining(titleContains));
        if (username != null)
            return ResponseEntity.ok(recipeService.getRecipesByUsername(username));
        if (usernameContains != null)
            return ResponseEntity.ok(recipeService.getRecipesByUsernameContaining(usernameContains));

        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @Operation(
            summary = "Add a recipe for the authenticated user",
            description = "Creates a new recipe for the authenticated user.")
    @PostMapping("/users/self/recipes")
    public ResponseEntity<String> addRecipeForSelf(@RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.addRecipeForSelf(recipeRequestDto);
        return new ResponseEntity<>("Recipe created successfully!",HttpStatus.CREATED);
    }

    @Operation(
            summary = "Add a new recipe for a specific user",
            description = "Creates a new recipe for the specified user. Only accessible by ADMIN users.")
    @PostMapping("/users/{userId}/recipes")
    public ResponseEntity<String> addRecipe(@PathVariable UUID userId,
                                            @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.addRecipe(userId,recipeRequestDto);
        return new ResponseEntity<>("Recipe created successfully!",HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a recipe for the authenticated user",
            description = "Updates an existing recipe, ensuring it belongs to the authenticated user.")
    @PutMapping("/recipes/self/{id}")
    public ResponseEntity<String> updateRecipeForSelf(@PathVariable UUID id,
                                                      @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.updateRecipe(id,recipeRequestDto,true);
        return ResponseEntity.ok("Recipe updated successfully!");
    }

    @Operation(
            summary = "Update a recipe",
            description = "Updates an existing recipe by its id. Only accessible by ADMIN users.")
    @PutMapping("/recipes/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable UUID id,
                                               @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.updateRecipe(id,recipeRequestDto,false);
        return ResponseEntity.ok("Recipe updated successfully!");
    }

    @Operation(
            summary = "Delete a recipe for the authenticated user",
            description = "Deletes a recipe by its id, ensuring it belongs to the authenticated user.")
    @DeleteMapping("/recipes/self/{id}")
    public ResponseEntity<String> deleteRecipeForSelf(@PathVariable UUID id){
        recipeService.deleteRecipe(id, true);
        return ResponseEntity.ok("Recipe with id: "+id+" deleted successfully!");
    }

    @Operation(
            summary = "Delete a recipe",
            description = "Deletes a recipe by its id. Only accessible by ADMIN users.")
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable UUID id){
        recipeService.deleteRecipe(id,false);
        return ResponseEntity.ok("Recipe with id: "+id+" deleted successfully!");
    }

}
