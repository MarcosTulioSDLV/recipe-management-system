package com.springboot.recipe_management_system.services;

import com.springboot.recipe_management_system.dtos.RoleResponseDto;
import com.springboot.recipe_management_system.models.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RoleService {

    Page<RoleResponseDto> getAllRoles(Pageable pageable);

    RoleResponseDto getRoleById(UUID id);

    Role findRoleById(UUID id);

    List<Role> findRolesByIds(List<UUID> ids);

    @Transactional
    void deleteRole(UUID id);
}
