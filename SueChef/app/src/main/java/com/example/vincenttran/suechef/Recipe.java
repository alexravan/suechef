package com.example.vincenttran.suechef;

import java.io.Serializable;

/**
 * Created by AlexanderRavan on 10/6/16.
 */

public class Recipe implements Serializable {
    public String imgUrl;
    public String title;
    public String description;
    public String[] directions;
    public String[] ingredients;

    public Recipe(String newTitle, String newImgUrl, String newDescription, String[] newIngredients, String[] newDirections) {
        imgUrl = newImgUrl;
        title = newTitle;
        description = newDescription;
        ingredients = newIngredients;
        directions = newDirections;
    }
}

