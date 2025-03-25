package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.models.Recipe;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RecipeService {

    RecipeResponseDto getRecipeById(UUID id,boolean isSelf);

    Recipe findRecipeById(UUID id);

    List<RecipeResponseDto> getAllRecipes();

    List<RecipeResponseDto> getAllRecipesForSelf();

    List<RecipeResponseDto> getRecipesByUsername(String username);

    List<RecipeResponseDto> getRecipesByUsernameContaining(String username);

    List<RecipeResponseDto> getRecipesByTitleForSelf(String title);

    List<RecipeResponseDto> getRecipesByTitle(String title);

    List<RecipeResponseDto> getRecipesByTitleContaining(String title);

    List<RecipeResponseDto> getRecipesByTitleContainingForSelf(String title);

    @Transactional
    void addRecipeForSelf(RecipeRequestDto recipeRequestDto);

    @Transactional
    void addRecipe(UUID userId, RecipeRequestDto recipeRequestDto);

    @Transactional
    void updateRecipe(UUID id, RecipeRequestDto recipeRequestDto, boolean isSelf);

    @Transactional
    void deleteRecipe(UUID id, boolean isSelf);

}
