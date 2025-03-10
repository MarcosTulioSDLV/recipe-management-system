package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.services.IngredientService;
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

import java.util.UUID;

@Tag(name = "Ingredient Controller", description = "Controller for managing ingredients.")
@RestController
@RequestMapping(value = "/api/v1")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Operation(
            summary = "Get an ingredient by id for the authenticated user",
            description = "Retrieves an ingredient by id, ensuring it belongs to the authenticated user.")
    @GetMapping("/ingredients/self/{id}")
    public ResponseEntity<IngredientResponseDto> getIngredientByIdForSelf(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id,true));
    }

    @Operation(
            summary = "Get an ingredient by id",
            description = "Retrieves an ingredient by id. Only accessible by users with ADMIN role.")
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientResponseDto> getIngredientById(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id,false));
    }

    @Operation(
            summary = "Get all ingredients for the authenticated user",
            description = "Retrieves a paginated list of ingredients belonging to the authenticated user.")
    @GetMapping("/ingredients/self")
    public ResponseEntity<Page<IngredientResponseDto>> getAllIngredientsForSelf(@PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(ingredientService.getAllIngredientsForSelf(pageable));
    }

    @Operation(
            summary = "Get all ingredients",
            description = "Retrieves a paginated list of all ingredients. Only accessible by users with ADMIN role.")
    @GetMapping("/ingredients")
    public ResponseEntity<Page<IngredientResponseDto>> getAllIngredients(@PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(ingredientService.getAllIngredients(pageable));
    }

    @Operation(
            summary = "Add an ingredient for the authenticated user",
            description = "Adds a new ingredient to a recipe owned by the authenticated user.")
    @PostMapping("/recipes/self/{recipeId}/ingredients")
    public ResponseEntity<String> addIngredientForSelf(@PathVariable UUID recipeId,
                                                       @RequestBody @Valid IngredientRequestDto ingredientRequestDto) {
        ingredientService.addIngredient(recipeId, ingredientRequestDto, true);
        return new ResponseEntity<>("Ingredient updated successfully!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Add an ingredient",
            description = "Adds a new ingredient to a recipe. Only accessible by users with ADMIN role.")
    @PostMapping("/recipes/{recipeId}/ingredients")
    public ResponseEntity<String> addIngredient(@PathVariable UUID recipeId,
                                                @RequestBody @Valid IngredientRequestDto ingredientRequestDto) {
        ingredientService.addIngredient(recipeId, ingredientRequestDto, false);
        return new ResponseEntity<>("Ingredient updated successfully!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an ingredient for the authenticated user",
            description = "Updates an ingredient by id, ensuring it belongs to the authenticated user.")
    @PutMapping("/ingredients/self/{id}")
    public ResponseEntity<String> updateIngredientForSelf(@PathVariable UUID id,
                                                          @RequestBody @Valid IngredientRequestDto ingredientRequestDto){
        ingredientService.updateIngredient(id,ingredientRequestDto,true);
        return ResponseEntity.ok("Ingredient updated successfully!");
    }

    @Operation(
            summary = "Update an ingredient",
            description = "Updates an ingredient by id. Only accessible by users with ADMIN role.")
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable UUID id,
                                                   @RequestBody @Valid IngredientRequestDto ingredientRequestDto){
        ingredientService.updateIngredient(id,ingredientRequestDto,false);
        return ResponseEntity.ok("Ingredient updated successfully!");
    }

    @Operation(
            summary = "Delete an ingredient for the authenticated user",
            description = "Deletes an ingredient by id, ensuring it belongs to the authenticated user.")
    @DeleteMapping("/ingredients/self/{id}")
    public ResponseEntity<String> deleteIngredientForSelf(@PathVariable UUID id){
        ingredientService.deleteIngredient(id,true);
        return ResponseEntity.ok("Ingredient with id:"+id+" removed successfully!");
    }

    @Operation(
            summary = "Delete an ingredient",
            description = "Deletes an ingredient by id. Only accessible by users with ADMIN role.")
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable UUID id){
        ingredientService.deleteIngredient(id, false);
        return ResponseEntity.ok("Ingredient with id:"+id+" removed successfully!");
    }

}
