package com.example.vincenttran.suechef;

import java.io.Serializable;

class Recipe implements Serializable {
    public final String imgUrl;
    public final String title;
    public final String description;
    public final String[] directions;
    public final String[] ingredients;

    public Recipe(String newTitle, String newImgUrl, String newDescription, String[] newIngredients, String[] newDirections) {
        imgUrl = newImgUrl;
        title = newTitle;
        description = newDescription;
        ingredients = newIngredients;
        directions = newDirections;
    }
}

