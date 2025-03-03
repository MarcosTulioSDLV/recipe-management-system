package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.exceptions.IngredientNotFoundException;
import com.springboot.recipe_management_system.exceptions.OwnershipException;
import com.springboot.recipe_management_system.mappers.IngredientMapper;
import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.Recipe;
import com.springboot.recipe_management_system.models.UserEntity;
import com.springboot.recipe_management_system.repositories.IngredientRepository;
import com.springboot.recipe_management_system.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void addIngredient(UUID recipeId, IngredientRequestDto ingredientRequestDto, boolean isSelf) {
        Recipe recipe= recipeService.findRecipeById(recipeId);
        if(isSelf){
            validateRecipeOwnership(recipe);
        }
        Ingredient ingredient= ingredientMapper.toIngredient(ingredientRequestDto);
        ingredient.setRecipe(recipe);
        ingredientRepository.save(ingredient);
    }

    private void validateRecipeOwnership(Recipe recipe) {
        validateOwnership(recipe,"Recipe does not belong to the current user!");
    }

    private void validateOwnership(Recipe recipe, String errorMessage) {
        String currentLoggedUsername= getCurrentLoggedUser().getUsername();
        if(!recipe.getUser().getUsername().equals(currentLoggedUsername))
            throw new OwnershipException(errorMessage);
    }

    private UserEntity getCurrentLoggedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)){
            System.out.println("Unauthorized access!");
            throw new AccessDeniedException("Unauthorized access!");
        }
        if(!(userDetails instanceof CustomUserDetails customUserDetails)){
            System.out.println("UserDetails is not an instance of CustomUserDetails!");
            throw new ClassCastException("UserDetails is not an instance of CustomUserDetails!");
        }
        return customUserDetails.getUser();
    }

    @Override
    @Transactional
    public void updateIngredient(UUID id, IngredientRequestDto ingredientRequestDto, boolean isSelf) {
        Ingredient ingredient= ingredientMapper.toIngredient(ingredientRequestDto);
        Ingredient recoveredIngredient= findIngredientById(id);
        if(isSelf){
            validateIngredientOwnership(recoveredIngredient);
        }
        BeanUtils.copyProperties(ingredient,recoveredIngredient,"id","recipe");
    }

    private void validateIngredientOwnership(Ingredient ingredient) {
        validateOwnership(ingredient.getRecipe(),"Ingredient does not belong to any recipe owned by the current user!");
    }

    @Override
    @Transactional
    public void deleteIngredient(UUID id, boolean isSelf) {
        Ingredient ingredient= findIngredientById(id);
        if(isSelf){
            validateIngredientOwnership(ingredient);
        }
        ingredientRepository.delete(ingredient);
    }

}
