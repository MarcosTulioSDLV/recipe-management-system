package com.springboot.recipe_management_system.models;

import com.springboot.recipe_management_system.enums.IngredientUnitEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "TB_INGREDIENT")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Ingredient {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "ingredient_unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private IngredientUnitEnum ingredientUnit;

    @Column(name = "ingredient_type", nullable = false)
    private String ingredientType;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    //Note: (alternative for UUID with mysql db)
    /*
    @PrePersist
    public void prePersist(){
        if(id==null)
            id= UUID.randomUUID();
    }*/

}
