package com.example.vincenttran.suechef;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private final List<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView mGridView = (GridView) findViewById(R.id.imageGridView);

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

        String[] urls = {"https://static01.nyt.com/images/2016/10/05/dining/05KITCH-WEB1/05KITCH-WEB1-articleLarge.jpg", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcRB0lFmcob-qKi9nmDxnCN68jpvOEbNOjcSyiOhQrWTf5YyOZ0tKPbrnlla", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIGDZz8ASXcIUYl7yKTY9pDnMaTBIzhycqCRkBdiRteEU855_4sA"};

        mGridView.setAdapter(new ImageListAdapter(MainActivity.this, urls));

    }


    public void showRecipe(View view) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        startActivity(intent);


    }
}
