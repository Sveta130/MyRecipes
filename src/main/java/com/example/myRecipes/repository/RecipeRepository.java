package com.example.myRecipes.repository;

import com.example.myRecipes.entity.RecipeEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {

    List<RecipeEntity> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<RecipeEntity> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);
}
