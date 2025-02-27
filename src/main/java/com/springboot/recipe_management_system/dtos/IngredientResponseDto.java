package com.springboot.recipe_management_system.dtos;

import com.springboot.recipe_management_system.enums.IngredientUnitEnum;
import com.springboot.recipe_management_system.models.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class IngredientResponseDto {

    private UUID id;

    private Double amount;

    private IngredientUnitEnum ingredientUnit;

    private String ingredientType;

}
