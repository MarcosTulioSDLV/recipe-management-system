package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.services.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1")
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Page<IngredientResponseDto>> getAllIngredients(@PageableDefault(size = 15) Pageable pageable) {
        return ResponseEntity.ok(ingredientService.getAllIngredients(pageable));
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<IngredientResponseDto> getIngredientById(@PathVariable UUID id) {
        return ResponseEntity.ok(ingredientService.getIngredientById(id));
    }

    @PostMapping("/recipes/self/{recipeId}/ingredients")
    public ResponseEntity<String> addIngredientForSelf(@PathVariable UUID recipeId,
                                                       @RequestBody @Valid IngredientRequestDto ingredientRequestDto) {
        ingredientService.addIngredient(recipeId, ingredientRequestDto, true);
        return new ResponseEntity<>("Ingredient updated successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/recipes/{recipeId}/ingredients")
    public ResponseEntity<String> addIngredient(@PathVariable UUID recipeId,
                                                @RequestBody @Valid IngredientRequestDto ingredientRequestDto) {
        ingredientService.addIngredient(recipeId, ingredientRequestDto, false);
        return new ResponseEntity<>("Ingredient updated successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/ingredients/self/{id}")
    public ResponseEntity<String> updateIngredientForSelf(@PathVariable UUID id,
                                                   @RequestBody @Valid IngredientRequestDto ingredientRequestDto){
        ingredientService.updateIngredient(id,ingredientRequestDto,true);
        return ResponseEntity.ok("Ingredient updated successfully!");
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable UUID id,
                                                   @RequestBody @Valid IngredientRequestDto ingredientRequestDto){
        ingredientService.updateIngredient(id,ingredientRequestDto,false);
        return ResponseEntity.ok("Ingredient updated successfully!");
    }

    @DeleteMapping("/ingredients/self/{id}")
    public ResponseEntity<String> deleteIngredientForSelf(@PathVariable UUID id){
        ingredientService.deleteIngredient(id,true);
        return ResponseEntity.ok("Ingredient with id:"+id+" removed successfully!");
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable UUID id){
        ingredientService.deleteIngredient(id, false);
        return ResponseEntity.ok("Ingredient with id:"+id+" removed successfully!");
    }

}
