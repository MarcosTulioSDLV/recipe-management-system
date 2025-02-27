package com.springboot.recipe_management_system.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank @Email
    private String email;

}
