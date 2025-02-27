package com.springboot.recipe_management_system.dtos;

import com.springboot.recipe_management_system.models.Role;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class UserResponseDto {

    private UUID id;

    private String username;

    private String email;

    private List<RoleResponseDto> roleResponseDtoList= new ArrayList<>();

}
