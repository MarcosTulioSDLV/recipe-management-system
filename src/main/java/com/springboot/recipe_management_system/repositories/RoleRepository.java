package com.springboot.recipe_management_system.repositories;

import com.springboot.recipe_management_system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,UUID> {


}
