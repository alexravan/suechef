package com.example.vincenttran.suechef;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.example.vincenttran.suechef.Recipe;



public class MainActivity extends AppCompatActivity {
//    private final List<String> urls = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView mGridView = (GridView) findViewById(R.id.imageGridView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Sue Chef");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

//        final Context context = this;
//        Ion.with(this)
//                .load("http://suechef.herokuapp.com/")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        if (e != null) {
//                            //TODO: something went wrong
//                            return;
//                        }
//
//                        String linkToImg = result.getAsJsonObject("recipe").get("img").getAsString();
////                        for (int i = 0; i < result.getAsJsonObject("recipe").get("img"))
//
//                        // TODO put into picasso
//                        Toast.makeText(MainActivity.this, linkToImg, Toast.LENGTH_SHORT).show();
//                    }
//                });

//        ng newTitle, String newImgUrl, String newDescription, String[] newIngredients, String[] newDirections) {

        /*** PIZZA ***/
        String[] ingredients = new String[] {"tomato", "cheese", "flour", "water", "yeast"};
        String[] directions  = new String[] {"Make the dough", "Assemble pizza", "Cook pizza"};
        Recipe pizza = new Recipe("Pizza", "http://www.989wolf.com/wp-content/uploads/2016/05/maxresdefault-1.jpg",
                                "this is a pizza", ingredients, directions);
        /*************/

        /*** COOKIES ***/
        ingredients = new String[] {"3 1/4 cups of flour", "1 teaspoon baking soda", "3/4 teaspoon salt", "1 1/3 cups butter, softened", "1 1/4 cups granulated sugar",
                                    "1 cup firmly packed light brown sugar", "2 eggs", "4 teaspoons McCormick® Pure Vanilla Extract", "1 (12 ounce) package semi-sweet chocolate chips",
                                    "1 cup chopped walnuts"};

        directions = new String[] {"Preheat oven to 375 degrees F. Mix flour, baking soda and salt in medium bowl. Set aside. Beat butter and sugars in large bowl with electric mixer on medium speed until light and fluffy. Add eggs and vanilla; mix well. Gradually beat in flour mixture on low speed until well mixed. Stir in chocolate chips and walnuts.",
                                    "Drop by rounded tablespoons about 2 inches apart onto ungreased baking sheets.",
                                    "Bake 8 to 10 minutes or until lightly browned. Cool on baking sheets 1 minute. Remove to wire racks; cool completely."};

        Recipe cookies = new Recipe("Vanilla Rich Chocolate Chip Cookies", "http://images.media-allrecipes.com/userphotos/720x405/3926474.jpg",
                "Flavors of premium vanilla and chocolate blend together to make a wonderful combination in these favorite chocolate chip cookies.",
                ingredients, directions);

        /****************/

        /*** COOKIES ***/
        ingredients = new String[] {"3 1/4 cups of flour", "1 teaspoon baking soda", "3/4 teaspoon salt", "1 1/3 cups butter, softened", "1 1/4 cups granulated sugar",
                "1 cup firmly packed light brown sugar", "2 eggs", "4 teaspoons McCormick® Pure Vanilla Extract", "1 (12 ounce) package semi-sweet chocolate chips",
                "1 cup chopped walnuts"};

        directions = new String[] {"Preheat oven to 375 degrees F. Mix flour, baking soda and salt in medium bowl. Set aside. Beat butter and sugars in large bowl with electric mixer on medium speed until light and fluffy. Add eggs and vanilla; mix well. Gradually beat in flour mixture on low speed until well mixed. Stir in chocolate chips and walnuts.",
                "Drop by rounded tablespoons about 2 inches apart onto ungreased baking sheets.",
                "Bake 8 to 10 minutes or until lightly browned. Cool on baking sheets 1 minute. Remove to wire racks; cool completely."};

        Recipe ramen = new Recipe("Tokushima Ramen", "http://images1.laweekly.com/imager/tokushima-ramen-at-men-oh-tokushima-ramen/u/original/4226846/men_oh_fukushima_ramen.jpg",
                "Flavors of premium vanilla and chocolate blend together to make a wonderful combination in these favorite chocolate chip cookies.",
                ingredients, directions);

        /****************/


        Recipe[] recipes = new Recipe[] {pizza, cookies, ramen,pizza, cookies, ramen,pizza, cookies, ramen,pizza, cookies, ramen,pizza, cookies, ramen,pizza, cookies, ramen};


        mGridView.setAdapter(new ImageListAdapter(MainActivity.this, recipes));

    }
}
