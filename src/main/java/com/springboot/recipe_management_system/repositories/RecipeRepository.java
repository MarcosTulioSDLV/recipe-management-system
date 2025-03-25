package com.springboot.recipe_management_system.repositories;

import com.springboot.recipe_management_system.models.Recipe;
import com.springboot.recipe_management_system.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,UUID> {

    List<Recipe> findAllByUser(UserEntity user);

    List<Recipe> findByUserUsernameIgnoreCase(String username);
    /*@Query(value = "SELECT r FROM Recipe r JOIN r.user u WHERE LOWER(username)=LOWER(:username)")
    List<Recipe> findByUserUsernameIgnoreCaseCustom(String username);*/

    List<Recipe> findByUserUsernameIgnoreCaseContaining(String username);
    /*@Query(value = "SELECT r FROM Recipe r JOIN r.user u WHERE LOWER(username) LIKE CONCAT('%',LOWER(:username),'%')")
    List<Recipe> findByUserUsernameIgnoreCaseContainingCustom(String username);*/

    List<Recipe> findByTitleIgnoreCase(String title);
    /*@Query(value = "SELECT r FROM Recipe r WHERE LOWER(r.title)=LOWER(:title)")
    List<Recipe> findByTitleIgnoreCaseCustom(String title);*/

    List<Recipe> findByTitleIgnoreCaseAndUser(String title, UserEntity user);

    List<Recipe> findByTitleIgnoreCaseContaining(String title);
    /*@Query(value = "SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE CONCAT('%',LOWER(:title),'%')")
    List<Recipe> findByTitleIgnoreCaseContainingCustom(String title);*/

    List<Recipe> findByTitleIgnoreCaseContainingAndUser(String title, UserEntity user);

}
