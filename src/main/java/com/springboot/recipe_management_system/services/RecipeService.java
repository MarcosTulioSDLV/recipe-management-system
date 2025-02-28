package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.models.Recipe;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RecipeService {

    List<RecipeResponseDto> getAllRecipes();

    RecipeResponseDto getRecipeById(UUID id);

    Recipe findRecipeById(UUID id);

    List<RecipeResponseDto> getRecipesByUsername(String username);

    List<RecipeResponseDto> getRecipesByUsernameContaining(String username);

    List<RecipeResponseDto> getRecipesByTitle(String title);

    List<RecipeResponseDto> getRecipesByTitleContaining(String title);

    @Transactional
    void addRecipe(UUID userId, RecipeRequestDto recipeRequestDto);

    @Transactional
    void updateRecipe(UUID id, RecipeRequestDto recipeRequestDto, boolean isSelf);

    @Transactional
    void deleteRecipe(UUID id);

}
