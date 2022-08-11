package com.example.myRecipes.repository;

import com.example.myRecipes.entity.IngredientEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {
}
