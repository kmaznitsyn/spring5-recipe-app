package com.example.spring5recipeapp.controllers;

import com.example.spring5recipeapp.commands.IngredientCommand;
import com.example.spring5recipeapp.commands.RecipeCommand;
import com.example.spring5recipeapp.services.IngredientServiceImpl;
import com.example.spring5recipeapp.services.RecipeServiceImpl;
import com.example.spring5recipeapp.services.UnitOfMeasureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @InjectMocks
    IngredientController ingredientController;

    @Mock
    RecipeServiceImpl recipeService;

    @Mock
    IngredientServiceImpl ingredientService;

    @Mock
    UnitOfMeasureServiceImpl unitOfMeasureService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void testListIngredients() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/2/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/list"))
                .andExpect(MockMvcResultMatchers.model().attribute("recipe", recipeCommand));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    void testShowIngredientById() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(2L);
        ingredientCommand.setId(1L);


        //when
        when(ingredientService.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/2/ingredient/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/show"))
                .andExpect(MockMvcResultMatchers.model().attribute("ingredient", ingredientCommand));
    }

    @Test
    void testUpdateIngredientForm() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        //when
        when(unitOfMeasureService.findAll()).thenReturn(new HashSet<>());
        when(ingredientService.findByRecipeIdAndId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/1/update"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("uomList", "ingredient"))
                .andExpect(view().name("recipe/ingredients/ingredientform"));

        verify(ingredientService, times(0)).findCommandById(anyLong());
    }

    @Test
    void testSaveOrUpdateIngredient() throws Exception {
        //given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);
        ingredientCommand.setRecipeId(2L);
        //when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/recipe/2/ingredient").contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/2/show"));
    }

    @Test
    void testNewIngredientForm() throws Exception {
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        //when
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        when(unitOfMeasureService.findAll()).thenReturn(new HashSet<>());

        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("uomList", "ingredient"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    void testDeleteIngredient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteById(anyLong(), anyLong());
    }
}