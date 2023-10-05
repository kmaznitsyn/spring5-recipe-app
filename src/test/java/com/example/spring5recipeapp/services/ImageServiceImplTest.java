package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Recipe;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    RecipeServiceImpl recipeService;

    @InjectMocks
    ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
    }

    @SneakyThrows
    @Test
    void saveImageFile() {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "imagefile", "testing.txt", "text/plain", "Spring kolya guru".getBytes()
        );
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImageFile(recipe.getId(), multipartFile);

        verify(recipeService, times(1)).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}