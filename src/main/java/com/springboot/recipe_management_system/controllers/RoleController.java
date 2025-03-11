package com.springboot.recipe_management_system.controllers;

import com.springboot.recipe_management_system.dtos.RoleResponseDto;
import com.springboot.recipe_management_system.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Role Controller", description = "Controller for managing roles")
@RestController
@RequestMapping(value = "/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(
            summary = "Get all roles",
            description = "Retrieves a paginated list of all roles. Only accessible by users with ADMIN role."
    )
    @GetMapping
    public ResponseEntity<Page<RoleResponseDto>> getAllRoles(@PageableDefault(size = 15) Pageable pageable){
        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }

    @Operation(
            summary = "Get role by id",
            description = "Retrieves a role by their id. Only accessible by users with ADMIN role."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable UUID id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @Operation(
            summary = "Delete role",
            description = "Deletes a role by id. Only accessible by users with ADMIN role."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable UUID id){
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role with id: "+id+" deleted successfully!");
    }

}
