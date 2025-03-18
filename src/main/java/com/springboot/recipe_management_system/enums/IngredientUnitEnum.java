package com.springboot.recipe_management_system.enums;

public enum IngredientUnitEnum {
    G("Gram"),     // Gram
    KG("Kilogram"),    // Kilogram
    ML("Milliliter"),    // Milliliter
    L("Liter"),     // Liter
    PC("Piece"),    // Piece (Unit)
    TSP("Teaspoon"),   // Teaspoon
    TBSP("Tablespoon"),  // Tablespoon
    PINCH("A dash");  // A dash, Small amount, typically using fingers

    private final String description;

    private IngredientUnitEnum(String description){
        this.description= description;
    }

    public String getDescription() {
        return description;
    }

}
