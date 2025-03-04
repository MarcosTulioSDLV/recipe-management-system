package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface IngredientService {

    IngredientResponseDto getIngredientById(UUID id, boolean isSelf);

    Page<IngredientResponseDto> getAllIngredients(Pageable pageable);

    Page<IngredientResponseDto> getAllIngredientsForSelf(Pageable pageable);

    @Transactional
    void addIngredient(UUID recipeId, IngredientRequestDto ingredientRequestDto, boolean isSelf);

    @Transactional
    void updateIngredient(UUID id, IngredientRequestDto ingredientRequestDto, boolean isSelf);

    @Transactional
    void deleteIngredient(UUID id, boolean isSelf);

}
