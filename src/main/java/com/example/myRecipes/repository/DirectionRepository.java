package com.example.myRecipes.repository;

import com.example.myRecipes.entity.DirectionEntity;
import com.example.myRecipes.entity.IngredientEntity;
import org.springframework.data.repository.CrudRepository;

public interface DirectionRepository extends CrudRepository<DirectionEntity, Long> {
}
