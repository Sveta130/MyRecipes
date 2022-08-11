package com.example.myRecipes.converter;

import com.example.myRecipes.dto.Recipe;
import com.example.myRecipes.entity.DirectionEntity;
import com.example.myRecipes.entity.IngredientEntity;
import com.example.myRecipes.entity.RecipeEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RecipeConverter {

    public Recipe toDto(RecipeEntity e) {
        Recipe recipe = new Recipe();
        recipe.setName(e.getName());
        recipe.setDescription(e.getDescription());
        recipe.setCategory(e.getCategory());
        recipe.setDate(e.getDate());

        List<String> ingredients = new ArrayList<>();
        for (IngredientEntity ingredient : e.getIngredients()) {
            ingredients.add(ingredient.getValue());
        }
        recipe.setIngredients(ingredients);

        List<String> directions = new ArrayList<>();
        for (DirectionEntity direction : e.getDirections()) {
            directions.add(direction.getValue());
        }
        recipe.setDirections(directions);

        return recipe;
    }
}
