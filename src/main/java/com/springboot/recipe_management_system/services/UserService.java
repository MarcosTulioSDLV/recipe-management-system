package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.AddUserRequestDto;
import com.springboot.recipe_management_system.dtos.UpdateUserRequestDto;
import com.springboot.recipe_management_system.dtos.loginUserRequestDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.models.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserForSelf();

    UserResponseDto getUserById(UUID id);

    UserEntity findUserById(UUID id);

    String loginUser(loginUserRequestDto loginUserRequestDto);

    @Transactional
    void addUser(AddUserRequestDto addUserRequestDto);

    @Transactional
    void updateUserForSelf(UpdateUserRequestDto updateUserRequestDto);

    @Transactional
    void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto);

    @Transactional
    void deleteUser(UUID id);

}
