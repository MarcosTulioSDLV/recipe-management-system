package com.springboot.recipe_management_system.mappers;

import com.springboot.recipe_management_system.dtos.AddUserRequestDto;
import com.springboot.recipe_management_system.dtos.RoleResponseDto;
import com.springboot.recipe_management_system.dtos.UpdateUserRequestDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.models.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;


    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserEntity toUser(AddUserRequestDto addUserRequestDto) {
        return modelMapper.map(addUserRequestDto,UserEntity.class);
    }

    public UserResponseDto toUserResponseDto(UserEntity user) {
        UserResponseDto userResponseDto= modelMapper.map(user,UserResponseDto.class);
        List<RoleResponseDto> roleResponseDtoList= user.getRoles().stream()
                .map(role-> RoleResponseDto.builder()
                        .id(role.getId())
                        .role(role.getRole())
                        .build())
                .collect(Collectors.toList());
        userResponseDto.setRoleResponseDtoList(roleResponseDtoList);
        return userResponseDto;
    }

    public UserEntity toUser(UpdateUserRequestDto updateUserRequestDto) {
        return modelMapper.map(updateUserRequestDto,UserEntity.class);
    }


}
