package com.springboot.recipe_management_system.mappers;

import com.springboot.recipe_management_system.dtos.IngredientRequestDto;
import com.springboot.recipe_management_system.dtos.IngredientResponseDto;
import com.springboot.recipe_management_system.models.Ingredient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public IngredientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public IngredientResponseDto toIngredientResponseDto(Ingredient ingredient){
        return modelMapper.map(ingredient, IngredientResponseDto.class);
    }

    public Ingredient toIngredient(IngredientRequestDto ingredientRequestDto){
        return modelMapper.map(ingredientRequestDto,Ingredient.class);
    }

}
