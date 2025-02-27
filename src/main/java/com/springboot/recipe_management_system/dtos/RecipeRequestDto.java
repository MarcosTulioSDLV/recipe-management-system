package com.springboot.recipe_management_system.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RecipeRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String instructions;

    private String description;

    private String servings;

    @NotNull @NotEmpty
    private List<@Valid IngredientRequestDto> ingredientRequestDtoList= new ArrayList<>();

}
