package com.vincenttran.suechef;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView mGridView = (GridView) findViewById(R.id.imageGridView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Sue Chef");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        Ion.with(this)
                .load("https://sue2.herokuapp.com/recipes")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (e != null) {                     // Error has occurred
                            Toast.makeText(MainActivity.this, "Our servers are having issues. Sorry about that!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Recipe[] recipes = new Recipe[result.size()];

                        for (int i = 0; i < result.size(); i++) {
                            JsonObject temp = result.get(i).getAsJsonObject();

                            String[] ingredients = parse(temp.get("ingredients").getAsJsonArray(), false);
                            String[] directions = parse(temp.get("instructions").getAsJsonArray(), true);
                            Recipe recipe = new Recipe(
                                    temp.get("title").getAsString(),
                                    temp.get("image_url").getAsString(),
                                    temp.get("description").getAsString(),
                                    ingredients,
                                    directions);

                            recipes[i] = recipe;
                        }

                        mGridView.setAdapter(new ImageListAdapter(MainActivity.this, recipes));
                    }
                });

    }

    // Takes a JsonArray (gson) of things to parse into a String[].
    // Also takes a boolean, true if elements to parse are in 'instruction' object
    // false if in 'ingredient' object
    private String[] parse(JsonArray elements, boolean instruction) {
        String objName;
        if (instruction)
            objName = "instruction";
        else
            objName = "ingredient";

        List<String> directionsList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            directionsList.add(elements.get(i).getAsJsonObject().get(objName).getAsString());
        }

        String[] directionsArr = new String[directionsList.size()];
        return directionsList.toArray(directionsArr);
    }
}
