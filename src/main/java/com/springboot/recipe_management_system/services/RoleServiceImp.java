package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RoleResponseDto;
import com.springboot.recipe_management_system.exceptions.RoleNotFoundException;
import com.springboot.recipe_management_system.models.Role;
import com.springboot.recipe_management_system.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImp implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleResponseDto> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(role-> RoleResponseDto.builder()
                        .id(role.getId())
                        .role(role.getRole())
                        .build());
    }

    @Override
    public RoleResponseDto getRoleById(UUID id) {
        Role role= findRoleById(id);
        return RoleResponseDto.builder()
                .id(role.getId())
                .role(role.getRole())
                .build();
    }

    @Override
    public Role findRoleById(UUID id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role with id: " + id + " not found!"));
    }

    @Override
    public List<Role> findRolesByIds(List<UUID> ids){
        return roleRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        Role role= findRoleById(id);
        roleRepository.delete(role);
    }

}

