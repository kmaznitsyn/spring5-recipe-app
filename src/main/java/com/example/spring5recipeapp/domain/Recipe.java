package com.example.spring5recipeapp.domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"notes", "ingredients", "categories"})
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;

    @Lob
    private String directions;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @Lob
    private Byte[] image;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Set<Category> getCategories() {
        return categories;
    }

    public void setNotes(Notes notes) {
        if (notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);
        return this;
    }

    public Recipe removeIngredient(Ingredient ingredient) {
        ingredient.setRecipe(null);
        this.ingredients.remove(ingredient);
        return this;
    }

    public static class Builder {
        private Recipe recipe;

        public Builder() {
            recipe = new Recipe();
        }

        public Builder withId(Long id) {
            recipe.id = id;
            return this;
        }

        public Recipe build() {
            return recipe;
        }
    }
}
