package com.springboot.recipe_management_system.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_USER")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString(exclude = {"recipes"})
//@EqualsAndHashCode(exclude = {"recipes"})
public class UserEntity {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    @ManyToMany
    @JoinTable(name = "TB_USER_ROLE",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles= new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Recipe> recipes= new ArrayList<>();

    //Note: (alternative for UUID with mysql db)
    /*@PrePersist
    public void prePersist() {
        if (id == null)
            id = UUID.randomUUID();
    }*/

}
