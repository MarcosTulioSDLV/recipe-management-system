package com.springboot.recipe_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_RECIPE")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString(exclude = {"ingredients"})
//@EqualsAndHashCode(exclude = {"ingredients"})
public class Recipe {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String instructions;

    @Column(columnDefinition = "TEXT")
    private String description;//Note: nullable field

    private String servings;//Note: nullable field

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL,orphanRemoval = true)//cascade = CascadeType.REMOVE
    private List<Ingredient> ingredients= new ArrayList<>();

    //Note: (alternative for UUID with mysql db)
    /*@PrePersist
    public void prePersist(){
       if(id==null)
           id= UUID.randomUUID();
    }*/

}
