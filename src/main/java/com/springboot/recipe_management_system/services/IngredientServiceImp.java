package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.exceptions.IngredientNotFoundException;
import com.springboot.recipe_management_system.mappers.IngredientMapper;
import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.Recipe;
import com.springboot.recipe_management_system.repositories.IngredientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IngredientServiceImp implements IngredientService{

    private final IngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper;

    private final RecipeService recipeService;

    @Autowired
    public IngredientServiceImp(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper, RecipeService recipeService) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
        this.recipeService = recipeService;
    }

    @Override
    public Page<IngredientResponseDto> getAllIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable).map(ingredientMapper::toIngredientResponseDto);
    }

    @Override
    public IngredientResponseDto getIngredientById(UUID id) {
        Ingredient ingredient= findIngredientById(id);
        return ingredientMapper.toIngredientResponseDto(ingredient);
    }

    private Ingredient findIngredientById(UUID id) {
        return ingredientRepository.findById(id).orElseThrow(() -> new IngredientNotFoundException("Ingredient with id: " + id + " not found!"));
    }

    @Override
    @Transactional
    public void addIngredient(UUID recipeId, IngredientRequestDto ingredientRequestDto) {
        Recipe recipe= recipeService.findRecipeById(recipeId);

        Ingredient ingredient= ingredientMapper.toIngredient(ingredientRequestDto);
        ingredient.setRecipe(recipe);
        ingredientRepository.save(ingredient);
    }

    @Override
    @Transactional
    public void updateIngredient(UUID id, IngredientRequestDto ingredientRequestDto) {
        Ingredient ingredient= ingredientMapper.toIngredient(ingredientRequestDto);
        Ingredient recoveredIngredient= findIngredientById(id);
        BeanUtils.copyProperties(ingredient,recoveredIngredient,"id","recipe");
    }

    @Override
    @Transactional
    public void deleteIngredient(UUID id) {
        Ingredient ingredient= findIngredientById(id);
        ingredientRepository.delete(ingredient);
    }

}
