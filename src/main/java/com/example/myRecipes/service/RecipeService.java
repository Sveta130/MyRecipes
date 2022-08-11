package com.example.myRecipes.service;

import com.example.myRecipes.converter.RecipeConverter;
import com.example.myRecipes.dto.Recipe;
import com.example.myRecipes.dto.UserDetailsImpl;
import com.example.myRecipes.entity.DirectionEntity;
import com.example.myRecipes.entity.IngredientEntity;
import com.example.myRecipes.entity.RecipeEntity;
import com.example.myRecipes.repository.DirectionRepository;
import com.example.myRecipes.repository.IngredientRepository;
import com.example.myRecipes.repository.RecipeRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final DirectionRepository directionRepository;
    private final RecipeConverter recipeConverter;

    public long save(Recipe recipe) {

        List<IngredientEntity> ingredients = new ArrayList<>();
        for (String ingredient : recipe.getIngredients()) {
            ingredients.add(ingredientRepository.save(new IngredientEntity(null, ingredient)));
        }

        List<DirectionEntity> directions = new ArrayList<>();
        for (String direction : recipe.getDirections()) {
            directions.add(directionRepository.save(new DirectionEntity(null, direction)));
        }

        UserDetailsImpl details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDateTime date = recipe.getDate() == null ? LocalDateTime.now() : recipe.getDate();
        RecipeEntity recipeEntity = new RecipeEntity(null, recipe.getName(), recipe.getCategory(),
            date, recipe.getDescription(), ingredients, directions, details.getUsername());

        return recipeRepository.save(recipeEntity).getId();
    }

    public Optional<Recipe> get(long id) {
        return recipeRepository.findById(id)
            .map(recipeConverter::toDto);
    }

    @Transactional
    public boolean delete(long id) {
        UserDetailsImpl details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<RecipeEntity> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            if (details.getUsername().equals(recipe.get().getOwnerEmail())) {
                recipeRepository.deleteById(id);
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            return false;
        }
    }

    @Transactional
    public boolean update(long id, Recipe newRecipe) {
        UserDetailsImpl details = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<RecipeEntity> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            if (details.getUsername().equals(recipe.get().getOwnerEmail())) {

                RecipeEntity entity = recipe.get();
                entity.setName(newRecipe.getName());
                entity.setDescription(newRecipe.getDescription());
                entity.setCategory(newRecipe.getCategory());

                entity.setDate(newRecipe.getDate() == null ? LocalDateTime.now() : newRecipe.getDate());

                List<IngredientEntity> ingredients = new ArrayList<>();
                for (String ingredient : newRecipe.getIngredients()) {
                    ingredients.add(ingredientRepository.save(new IngredientEntity(null, ingredient)));
                }
                entity.setIngredients(ingredients);

                List<DirectionEntity> directions = new ArrayList<>();
                for (String direction : newRecipe.getDirections()) {
                    directions.add(directionRepository.save(new DirectionEntity(null, direction)));
                }
                entity.setDirections(directions);

                recipeRepository.save(entity);
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            return false;
        }
    }

    public List<Recipe> searchByCategory(String category) {
        return recipeRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category)
            .stream()
            .map(recipeConverter::toDto)
            .collect(Collectors.toList());
    }

    public List<Recipe> searchByName(String name) {
        return recipeRepository.findAllByNameContainingIgnoreCaseOrderByDateDesc(name)
            .stream()
            .map(recipeConverter::toDto)
            .collect(Collectors.toList());
    }
}
