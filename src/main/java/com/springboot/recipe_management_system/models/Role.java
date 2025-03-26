package com.springboot.recipe_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.recipe_management_system.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString(exclude = {"users"})
//@EqualsAndHashCode(exclude = {"users"})
public class Role {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false,unique = true)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles",cascade = CascadeType.REMOVE)
    private List<UserEntity> users= new ArrayList<>();

}
