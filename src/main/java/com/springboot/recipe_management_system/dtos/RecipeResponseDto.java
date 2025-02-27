package com.springboot.recipe_management_system.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class RecipeResponseDto {

    private UUID id;

    private String title;

    private String instructions;

    private String description;

    private String servings;

    private UserResponseDto userResponseDto;

    private List<IngredientResponseDto> ingredientResponseDtoList= new ArrayList<>();

}
