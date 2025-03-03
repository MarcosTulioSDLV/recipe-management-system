package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.exceptions.OwnershipException;
import com.springboot.recipe_management_system.exceptions.RecipeNotFoundException;
import com.springboot.recipe_management_system.mappers.RecipeMapper;
import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.Recipe;
import com.springboot.recipe_management_system.models.UserEntity;
import com.springboot.recipe_management_system.repositories.RecipeRepository;
import com.springboot.recipe_management_system.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<RecipeResponseDto> getAllRecipesForSelf() {
        UserEntity currentLoggedUser= getCurrentLoggedUser();
        return recipeRepository.findAllByUser(currentLoggedUser).stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponseDto getRecipeById(UUID id,boolean isSelf) {
        Recipe recipe= findRecipeById(id);
        if(isSelf){
            validateRecipeOwnership(recipe);
        }
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
    public List<RecipeResponseDto> getRecipesByTitleForSelf(String title) {
        UserEntity currentLoggedUser= getCurrentLoggedUser();
        return recipeRepository.findByTitleIgnoreCaseAndUser(title,currentLoggedUser)
                .stream()
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
    public List<RecipeResponseDto> getRecipesByTitleContainingForSelf(String title) {
        UserEntity currentLoggedUser= getCurrentLoggedUser();
        return recipeRepository.findByTitleIgnoreCaseContainingAndUser(title,currentLoggedUser)
                .stream()
                .map(recipeMapper::toRecipeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addRecipeForSelf(RecipeRequestDto recipeRequestDto) {
        addRecipe(null, recipeRequestDto);
    }

    @Override
    @Transactional
    public void addRecipe(UUID userId, RecipeRequestDto recipeRequestDto) {
        UserEntity user = (userId == null) ? getCurrentLoggedUser() : userService.findUserById(userId);

        Recipe recipe = recipeMapper.toRecipe(recipeRequestDto);
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
    public void updateRecipe(UUID id, RecipeRequestDto recipeRequestDto, boolean isSelf) {
        Recipe recipe= recipeMapper.toRecipe(recipeRequestDto);
        cleanOptionalFields(recipe);

        Recipe recoveredRecipe= findRecipeById(id);
        if(isSelf) {
            validateRecipeOwnership(recoveredRecipe);
        }
        recoveredRecipe.getIngredients().clear();

        List<Ingredient> ingredients= recipe.getIngredients();
        ingredients.forEach(ingredient->ingredient.setRecipe(recoveredRecipe));
        recoveredRecipe.getIngredients().addAll(ingredients);

        BeanUtils.copyProperties(recipe,recoveredRecipe,"id","user","ingredients");
        //recipeRepository.save(recoveredRecipe);
    }

    //Note: Validate if the recipe belongs to the current logged-in user
    private void validateRecipeOwnership(Recipe recipe) {
        String currentLoggedUsername= getCurrentLoggedUser().getUsername();
        if(!recipe.getUser().getUsername().equals(currentLoggedUsername))
            throw new OwnershipException("Recipe does not belong to the current user!");
    }

    private UserEntity getCurrentLoggedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        /*if(authentication!=null){
            System.out.println("Authentication class: " + authentication.getClass().getName());
            System.out.println("Authorities: " + authentication.getAuthorities());
            System.out.println("Is Not Authenticated: "+!authentication.isAuthenticated());
            System.out.println("Is Not UserDetails: "+!(authentication.getPrincipal() instanceof UserDetails));
        }*/
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
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
    public void deleteRecipe(UUID id, boolean isSelf) {
        Recipe recipe= findRecipeById(id);
        if(isSelf){
            validateRecipeOwnership(recipe);
        }
        recipeRepository.delete(recipe);
    }

}
