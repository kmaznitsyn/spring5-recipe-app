package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.commands.IngredientCommand;
import com.example.spring5recipeapp.domain.Ingredient;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndId(Long recipeId, Long id);

    Ingredient findById(Long id);

    IngredientCommand findCommandById(Long id);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(Long recipeId, Long id);
}
