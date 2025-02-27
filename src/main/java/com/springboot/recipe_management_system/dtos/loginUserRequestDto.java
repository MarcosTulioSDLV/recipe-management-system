package com.springboot.recipe_management_system.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class loginUserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
