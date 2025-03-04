package com.springboot.recipe_management_system.repositories;

import com.springboot.recipe_management_system.models.Ingredient;
import com.springboot.recipe_management_system.models.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient,UUID> {

    Page<Ingredient> findAllByRecipe_User(UserEntity currentLoggedUser, Pageable pageable);


    //TO DO: TESTING
    @Query("SELECT i FROM Ingredient i JOIN i.recipe r JOIN r.user u WHERE u=:currentLoggedUSer")
    Page<Ingredient> findAllByRecipe_UserCustom(UserEntity currentLoggedUser, Pageable pageable);

    @Query(value="SELECT i.* FROM TB_INGREDIENT i INNER JOIN TB_RECIPE r ON i.recipe_id=r.id\n" +
            " INNER JOIN TB_USER u ON r.user_id=u.id WHERE u.id=:userId",nativeQuery = true)
    Page<Ingredient> findAllByUserIdCustomQuery(UUID userId, Pageable pageable);

}
