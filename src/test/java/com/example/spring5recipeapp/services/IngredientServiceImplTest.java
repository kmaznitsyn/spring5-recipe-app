package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.commands.IngredientCommand;
import com.example.spring5recipeapp.commands.UnitOfMeasureCommand;
import com.example.spring5recipeapp.converters.IngredientCommandToIngredient;
import com.example.spring5recipeapp.converters.IngredientToIngredientCommand;
import com.example.spring5recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import com.example.spring5recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.example.spring5recipeapp.domain.Ingredient;
import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.domain.UnitOfMeasure;
import com.example.spring5recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

    private IngredientToIngredientCommand ingredientToIngredientCommand;
    private IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureRepository, recipeService);
    }

    @Mock
    private RecipeService recipeService;

    IngredientServiceImpl ingredientService;

    @Test
    void findByRecipeIdAndId() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient);
        recipe.addIngredient(ingredient3);
        recipe.addIngredient(ingredient2);



        //when
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        //then
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndId(1L, 2L);

        assertEquals(1L, ingredientCommand.getRecipeId());
        assertEquals(2L, ingredientCommand.getId());
        verify(recipeService, times(1)).findById(anyLong());
    }

    @Test
    void testSaveIngredient() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(2L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        recipe.addIngredient(ingredient);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(2L);
        ingredientCommand.setId(3L);
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1L);
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(1L);
        ingredientCommand.setUom(unitOfMeasureCommand);
        Optional<UnitOfMeasure> optionalUnitOfMeasure =  Optional.of(unitOfMeasure);
        //when
        when(recipeService.findById(anyLong())).thenReturn(recipe);
        when(unitOfMeasureRepository.findById(anyLong())).thenReturn(optionalUnitOfMeasure);
        when(recipeService.save(any())).thenReturn(recipe);
        //then
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        assertEquals(3L, savedCommand.getId());
        verify(recipeService, times(1)).findById(anyLong());
        verify(recipeService, times(1)).save(recipe);
    }

    @Test
    void testDeleteIngredientById() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(2L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2L);
        recipe.addIngredient(ingredient);
        //when
        when(recipeService.findById(anyLong())).thenReturn(recipe);
        ingredientService.deleteById(2L, 2L);
        //then
        verify(recipeService, times(1)).save(any());
        verify(recipeService, times(1)).findById(anyLong());
    }
}