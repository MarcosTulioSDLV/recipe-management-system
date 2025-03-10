package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.AddUserRequestDto;
import com.springboot.recipe_management_system.dtos.UpdateUserRequestDto;
import com.springboot.recipe_management_system.dtos.loginUserRequestDto;
import com.springboot.recipe_management_system.dtos.UserResponseDto;
import com.springboot.recipe_management_system.exceptions.*;
import com.springboot.recipe_management_system.mappers.UserMapper;
import com.springboot.recipe_management_system.models.Role;
import com.springboot.recipe_management_system.models.UserEntity;
import com.springboot.recipe_management_system.repositories.UserRepository;
import com.springboot.recipe_management_system.security.CustomUserDetails;
import com.springboot.recipe_management_system.security.util.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImp(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleService roleService, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponseDto);
    }

    @Override
    public UserResponseDto getUserForSelf() {
        UserEntity currentLoggedUser= getCurrentLoggedUser();
        return getUserById(currentLoggedUser.getId());
    }

    private UserEntity getCurrentLoggedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            System.out.println("Unauthorized access!");
            throw new AccessDeniedException("Unauthorized access!");
        }
        if(!(userDetails instanceof CustomUserDetails customUserDetails)){
            System.out.println("UserDetails is not an instance of CustomUserDetails!");
            throw new ClassCastException("UserDetails is not an instance of CustomUserDetails!");
        }
        return customUserDetails.getUser();
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        UserEntity user= findUserById(id);
        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserEntity findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found!"));
    }

    @Override
    public String loginUser(loginUserRequestDto loginUserRequestDto) {
        String username= loginUserRequestDto.getUsername();
        String password= loginUserRequestDto.getPassword();

        var passwordAuthenticationToken= new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication= authenticationManager.authenticate(passwordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtUtils.generateToken(authentication);
    }

    @Override
    @Transactional
    public void addUser(AddUserRequestDto addUserRequestDto) {
        UserEntity user= userMapper.toUser(addUserRequestDto);
        validateUniqueFields(user);

        String encodedPassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        List<UUID> roleIds= addUserRequestDto.getRoleIds();
        List<Role> roles= roleService.findRolesByIds(roleIds);
        validateRoleIds(roles);

        user.setRoles(roles);
        userRepository.save(user);
    }

    private void validateUniqueFields(UserEntity user) {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameExistsException("Username: "+ user.getUsername()+" already exists!");
        }
        if(userRepository.existsByEmailIgnoreCase(user.getEmail())){
            throw new UserEmailExistsException("User email: "+ user.getEmail()+" already exists!");
        }
    }

    private void validateRoleIds(List<Role> roles) {
        if(roles.isEmpty())
            throw new RoleIdsNotFoundException("Roles with ids not found!");
    }

    @Override
    @Transactional
    public void updateUserForSelf(UpdateUserRequestDto updateUserRequestDto) {
       updateUser(null, updateUserRequestDto);
    }

    @Override
    @Transactional
    public void updateUser(UUID id, UpdateUserRequestDto updateUserRequestDto) {
        UserEntity user= userMapper.toUser(updateUserRequestDto);
        String encodedPassword= passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserEntity recoveredUser= id==null ? findUserById(getCurrentLoggedUser().getId()) : findUserById(id);
        System.out.println(recoveredUser);
        validateFieldsUpdateConflict(user, recoveredUser);

        BeanUtils.copyProperties(user,recoveredUser,"id","roles","recipes");//Note:Ignore relationship properties
    }

    private void validateFieldsUpdateConflict(UserEntity user, UserEntity recoveredUser) {
        if(usernameExistsForAnotherUser(user.getUsername(), recoveredUser)){
            throw new UsernameExistsException("Username: "+ user.getUsername()+" already exists!");
        }
        if(userEmailExistsForAnotherUser(user.getEmail(), recoveredUser)){
            throw new UserEmailExistsException("User email: "+ user.getEmail()+" already exists!");
        }
    }

    private boolean usernameExistsForAnotherUser(String username, UserEntity recoveredUser) {
        return userRepository.existsByUsernameAndIdNot(username,recoveredUser.getId());
    }

    private boolean userEmailExistsForAnotherUser(String email, UserEntity recoveredUser) {
        return userRepository.existsByEmailIgnoreCaseAndIdNot(email,recoveredUser.getId());
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        UserEntity user= findUserById(id);
        userRepository.delete(user);
    }

}
