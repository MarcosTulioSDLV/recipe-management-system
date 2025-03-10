package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.AddUserRequestDto;
import com.springboot.recipe_management_system.dtos.UpdateUserRequestDto;
import com.springboot.recipe_management_system.dtos.loginUserRequestDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User Controller", description = "Controller for managing users")
@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users. Only accessible by users with ADMIN role.")
    @GetMapping()
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@PageableDefault(size = 15) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @Operation(
            summary = "Get the current logged user",
            description = "Retrieves the current logged user.")
    @GetMapping("/self")
    public ResponseEntity<UserResponseDto> getUserForSelf(){
        return ResponseEntity.ok(userService.getUserForSelf());
    }

    @Operation(
            summary = "Get user by id",
            description = "Retrieves a user by their id. Only accessible by users with ADMIN role.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid loginUserRequestDto loginUserRequestDto){
        return ResponseEntity.ok(userService.loginUser(loginUserRequestDto));
    }

    @Operation(summary = "Register new user",
            description = "Creates a new user account. Only accessible by users with ADMIN role.")
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody @Valid AddUserRequestDto addUserRequestDto){
        userService.addUser(addUserRequestDto);
        return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update the current logged user",
            description = "Updates the current logged user.")
    @PutMapping("/self")
    public ResponseEntity<String> updateUserForSelf(@RequestBody @Valid UpdateUserRequestDto updateUserRequestDto){
        userService.updateUserForSelf(updateUserRequestDto);
        return ResponseEntity.ok("User updated successfully!");
    }

    @Operation(
            summary = "Update user",
            description = "Updates user information by id. Only accessible by users with ADMIN role.")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id,
                                             @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto){
        userService.updateUser(id,updateUserRequestDto);
        return ResponseEntity.ok("User updated successfully!");
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their id. Only accessible by users with ADMIN role.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

}
