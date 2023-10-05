package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Recipe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeService recipeService;

    public ImageServiceImpl(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile multipartFile) {
        log.debug("Saving file for recipe with id " + recipeId);

        Recipe recipe = recipeService.findById(recipeId);
        if (recipe == null) {
            throw new RuntimeException("Recipe not found with id " + recipeId);
        }

        try {
            Byte[] byteObjects = new Byte[multipartFile.getBytes().length];

            for (int i = 0; i < byteObjects.length; i++) {
                byteObjects[i] = multipartFile.getBytes()[i];
            }

            recipe.setImage(byteObjects);
            recipeService.save(recipe);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
