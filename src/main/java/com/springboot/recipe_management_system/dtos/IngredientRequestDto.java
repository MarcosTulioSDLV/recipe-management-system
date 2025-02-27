package com.springboot.recipe_management_system.dtos;

import com.springboot.recipe_management_system.enums.IngredientUnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class IngredientRequestDto {

    @NotNull @PositiveOrZero
    private Double amount;

    @NotNull
    private IngredientUnitEnum ingredientUnit;

    @NotBlank
    private String ingredientType;

}
