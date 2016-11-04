package com.example.vincenttran.suechef;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import com.example.vincenttran.suechef.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecognitionListener{
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        final Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(recipe.title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView img = (ImageView) findViewById(R.id.recipeDetailImg);
        // Set contentDescription
        img.setContentDescription(recipe.title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeActivity.this, StepsActivity.class);
                List<String> directionsList = parseDirections(recipe.directions);
                String[] directionsArray = new String[directionsList.size()];
                directionsArray = directionsList.toArray(directionsArray);
                intent.putExtra("directions", directionsArray);
                startActivity(intent);
            }
        });

        Picasso
                .with(this)
                .load(recipe.imgUrl)
                .fit()
                .into(img);

        // get the listview
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expandableList);

        // preparing list data
        prepareListData(recipe.description, recipe.ingredients, recipe.directions);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData(String description, String[] ingredients, String[] directions) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Description");
        listDataHeader.add("Ingredients");
        listDataHeader.add("Directions");

        List<String> descriptionList = new ArrayList<>();
        descriptionList.add(description);

        List<String> ingredientsList = Arrays.asList(ingredients);
        List<String> directionsList = parseDirections(directions);

        listDataChild.put(listDataHeader.get(0), descriptionList);
        listDataChild.put(listDataHeader.get(1), ingredientsList);
        listDataChild.put(listDataHeader.get(2), directionsList);

    }

    public List<String> parseDirections(String[] directions) {
        List splitDirections = new ArrayList<String>();
        for (String s : directions) {
            String[] parts = s.split("\\. ");
            splitDirections.addAll(Arrays.asList(parts));
        }
        return splitDirections;
    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {

    }

    @Override
    public void onTimeout() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {


    }

    @Override
    public void onError(Exception e) {

    }
}
