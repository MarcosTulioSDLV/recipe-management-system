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

    Page<IngredientResponseDto> getAllIngredients(Pageable pageable);

    IngredientResponseDto getIngredientById(UUID id);

    @Transactional
    void addIngredient(UUID recipeId, IngredientRequestDto ingredientRequestDto, boolean isSelf);

    @Transactional
    void updateIngredient(UUID id, IngredientRequestDto ingredientRequestDto, boolean isSelf);

    @Transactional
    void deleteIngredient(UUID id, boolean isSelf);

}
