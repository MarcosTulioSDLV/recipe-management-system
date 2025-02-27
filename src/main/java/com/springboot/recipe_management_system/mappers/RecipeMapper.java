package com.springboot.recipe_management_system.mappers;

import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.dtos.RecipeRequestDto;
import com.springboot.recipe_management_system.dtos.RecipeResponseDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.Recipe;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {

    private final ModelMapper modelMapper;

    private final UserMapper userMapper;

    private final IngredientMapper ingredientMapper;

    @Autowired
    public RecipeMapper(ModelMapper modelMapper, UserMapper userMapper, IngredientMapper ingredientMapper) {
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.ingredientMapper = ingredientMapper;
    }

    public RecipeResponseDto toRecipeResponseDto(Recipe recipe){
        RecipeResponseDto recipeResponseDto= modelMapper.map(recipe,RecipeResponseDto.class);
        UserResponseDto userResponseDto= userMapper.toUserResponseDto(recipe.getUser());

        List<IngredientResponseDto> ingredientResponseDtoList= recipe.getIngredients().stream()
                .map(ingredientMapper::toIngredientResponseDto)
                .collect(Collectors.toList());

        recipeResponseDto.setUserResponseDto(userResponseDto);
        recipeResponseDto.setIngredientResponseDtoList(ingredientResponseDtoList);

        setNullOptionalFieldsToEmptyString(recipeResponseDto);
        return recipeResponseDto;
    }

    private void setNullOptionalFieldsToEmptyString(RecipeResponseDto recipeResponseDto) {
        if(recipeResponseDto.getDescription()==null)
            recipeResponseDto.setDescription("");
        if(recipeResponseDto.getServings()==null)
            recipeResponseDto.setServings("");
    }

    public Recipe toRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe= modelMapper.map(recipeRequestDto,Recipe.class);
        List<Ingredient> ingredients= recipeRequestDto.getIngredientRequestDtoList().stream()
                .map(ingredientMapper::toIngredient)
                .collect(Collectors.toList());
        recipe.setIngredients(ingredients);
        return recipe;
    }

}
