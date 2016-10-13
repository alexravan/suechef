package com.example.vincenttran.suechef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import com.example.vincenttran.suechef.Recipe;



public class MainActivity extends AppCompatActivity {
    private final List<String> urls = new ArrayList<String>();



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

        final Context context = this;
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
        String[] ingredients = new String[] {"tomato", "cheese", "flour", "water", "yeast"};
        String[] directions  = new String[] {"Make the dough", "Assemble pizza", "Cook pizza"};



        Recipe pizza = new Recipe("Pizza", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRB0lFmcob-qKi9nmDxnCN68jpvOEbNOjcSyiOhQrWTf5YyOZ0tKPbrnlla",
                                "this is a pizza", ingredients, directions);
        Recipe[] recipes = new Recipe[] {pizza};


        mGridView.setAdapter(new ImageListAdapter(MainActivity.this, recipes));

    }


    public void showRecipe(View view) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        startActivity(intent);


    }
}
