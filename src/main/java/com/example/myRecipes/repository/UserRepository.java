package com.example.myRecipes.repository;

import com.example.myRecipes.entity.IngredientEntity;
import com.example.myRecipes.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
