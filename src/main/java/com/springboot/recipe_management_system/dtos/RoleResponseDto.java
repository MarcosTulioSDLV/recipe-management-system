package com.springboot.recipe_management_system.dtos;

import com.springboot.recipe_management_system.enums.RoleEnum;
import com.springboot.recipe_management_system.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Builder
@AllArgsConstructor
public class RoleResponseDto {

    private UUID id;

    private RoleEnum role;

    public RoleResponseDto(){
    }

}
