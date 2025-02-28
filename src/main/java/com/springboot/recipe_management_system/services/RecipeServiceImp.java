package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.exceptions.RecipeNotFoundException;
import com.springboot.recipe_management_system.mappers.RecipeMapper;
import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.Recipe;
import com.springboot.recipe_management_system.models.UserEntity;
import com.springboot.recipe_management_system.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImp implements RecipeService{

    private final RecipeRepository recipeRepository;

    private final RecipeMapper recipeMapper;

    private final UserService userService;

    @Autowired
    public RecipeServiceImp(RecipeRepository recipeRepository, RecipeMapper recipeMapper, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.userService = userService;
    }

    @Override
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeRepository.findAll().stream().map(recipeMapper::toRecipeResponseDto).toList();
    }

    @Override
    public RecipeResponseDto getRecipeById(UUID id) {
        Recipe recipe= findRecipeById(id);
        return recipeMapper.toRecipeResponseDto(recipe);
    }

    @Override
    public Recipe findRecipeById(UUID id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe with id: " + id + " not found!"));
    }

    @Override
    public List<RecipeResponseDto> getRecipesByUsername(String username) {
        return recipeRepository.findByUserUsernameIgnoreCase(username).stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponseDto> getRecipesByUsernameContaining(String username) {
        return recipeRepository.findByUserUsernameIgnoreCaseContaining(username).stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponseDto> getRecipesByTitle(String title) {
        return recipeRepository.findByTitleIgnoreCase(title).stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeResponseDto> getRecipesByTitleContaining(String title) {
        return recipeRepository.findByTitleIgnoreCaseContaining(title).stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addRecipe(UUID userId, RecipeRequestDto recipeRequestDto) {
        UserEntity user= userService.findUserById(userId);

        Recipe recipe= recipeMapper.toRecipe(recipeRequestDto);
        cleanOptionalFields(recipe);

        recipe.setUser(user);
        recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe));

        recipeRepository.save(recipe);
    }

    private void cleanOptionalFields(Recipe recipe) {
        recipe.setDescription(cleanOptionalString(recipe.getDescription()));
        recipe.setServings(cleanOptionalString(recipe.getServings()));
    }

    private String cleanOptionalString(String str) {
        return str!=null && str.trim().isEmpty()?null:str;
    }

    @Override
    @Transactional
    public void updateRecipe(UUID id, RecipeRequestDto recipeRequestDto,boolean isSelf) {
        Recipe recipe= recipeMapper.toRecipe(recipeRequestDto);
        cleanOptionalFields(recipe);

        Recipe recoveredRecipe= findRecipeById(id);
        //validate current recipe is one of my recipes
        if(isSelf)
            validateRecipeIsMine(recoveredRecipe.getId());

        recoveredRecipe.getIngredients().clear();

        List<Ingredient> ingredients= recipe.getIngredients();
        ingredients.forEach(ingredient->ingredient.setRecipe(recoveredRecipe));
        recoveredRecipe.getIngredients().addAll(ingredients);

        BeanUtils.copyProperties(recipe,recoveredRecipe,"id","user","ingredients");
        //recipeRepository.save(recoveredRecipe);
    }

    private void validateRecipeIsMine(UUID recipeId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null /*|| !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)*/) {
            System.out.println("FIRST EXC-------------->");
            throw new AccessDeniedException("Unauthorized access");
        }
        String username= authentication.getName();
        System.out.println("username: "+username);
        if(recipeRepository.existsByIdAndUserUsernameNot(recipeId,username)){
            System.out.println("SECOND EXC-------------->");
            throw new AccessDeniedException("Recipe does not belong to the current user");
        }
    }


    @Override
    @Transactional
    public void deleteRecipe(UUID id) {
        Recipe recipe= findRecipeById(id);
        recipeRepository.delete(recipe);
    }

}
