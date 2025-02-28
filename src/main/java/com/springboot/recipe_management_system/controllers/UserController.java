package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.AddUserRequestDto;
import com.springboot.recipe_management_system.dtos.UpdateUserRequestDto;
import com.springboot.recipe_management_system.dtos.loginUserRequestDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@PageableDefault(size = 15) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid loginUserRequestDto loginUserRequestDto){
        return ResponseEntity.ok(userService.loginUser(loginUserRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody @Valid AddUserRequestDto addUserRequestDto){
        userService.addUser(addUserRequestDto);
        return new ResponseEntity<>("User created successfully!", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable UUID id,
                                             @RequestBody @Valid UpdateUserRequestDto updateUserRequestDto){
        userService.updateUser(id,updateUserRequestDto);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

}
