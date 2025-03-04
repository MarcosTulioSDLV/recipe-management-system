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

    @GetMapping("/recipes/self/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeByIdForSelf(@PathVariable UUID id){
        return ResponseEntity.ok(recipeService.getRecipeById(id,true));
    }

    @GetMapping("/recipes/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable UUID id){
        return ResponseEntity.ok(recipeService.getRecipeById(id,false));
    }

    /*

    @GetMapping("/recipes/search")
    public ResponseEntity<Page<RecipeResponseDto>> getAllRecipes(@PageableDefault(size = 15) Pageable pageable){
        return ResponseEntity.ok(recipeService.getAllRecipes(pageable));
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
    */

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

    @PostMapping("/users/self/recipes")
    public ResponseEntity<String> addRecipeForSelf(@RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.addRecipeForSelf(recipeRequestDto);
        return new ResponseEntity<>("Recipe created successfully!",HttpStatus.CREATED);
    }

    @PostMapping("/users/{userId}/recipes")
    public ResponseEntity<String> addRecipe(@PathVariable UUID userId,
                                            @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.addRecipe(userId,recipeRequestDto);
        return new ResponseEntity<>("Recipe created successfully!",HttpStatus.CREATED);
    }

    @PutMapping("/recipes/self/{id}")
    public ResponseEntity<String> updateRecipeForSelf(@PathVariable UUID id,
                                                      @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.updateRecipe(id,recipeRequestDto,true);
        return ResponseEntity.ok("Recipe updated successfully!");
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<String> updateRecipe(@PathVariable UUID id,
                                               @RequestBody @Valid RecipeRequestDto recipeRequestDto){
        recipeService.updateRecipe(id,recipeRequestDto,false);
        return ResponseEntity.ok("Recipe updated successfully!");
    }

    @DeleteMapping("/recipes/self/{id}")
    public ResponseEntity<String> deleteRecipeForSelf(@PathVariable UUID id){
        recipeService.deleteRecipe(id, true);
        return ResponseEntity.ok("Recipe with id: "+id+" deleted successfully!");
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable UUID id){
        recipeService.deleteRecipe(id,false);
        return ResponseEntity.ok("Recipe with id: "+id+" deleted successfully!");
    }

}
