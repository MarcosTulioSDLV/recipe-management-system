package com.springboot.recipe_management_system.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class AddUserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank @Email
    private String email;

    @NotNull @NotEmpty
    private List<UUID> roleIds= new ArrayList<>();

}
