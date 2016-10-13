package com.example.vincenttran.suechef;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import com.example.vincenttran.suechef.Recipe;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecognitionListener{
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ImageView img = (ImageView) findViewById(R.id.recipeDetailImg);
//        TextView title = (TextView) findViewById(R.id.recipeDetailTitle);
//        TextView description = (TextView) findViewById(R.id.recipeDetailDesc);
//        ListView ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
//        ListView directionsListView = (ListView) findViewById(R.id.directionsListView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeActivity.this, StepsActivity.class);
                intent.putExtra("directions", recipe.directions);
                startActivity(intent);
            }
        });

        setTitle(recipe.title);

        Picasso
                .with(this)
                .load(recipe.imgUrl)
                .fit()
                .into(img);
//
//        title.setText(recipe.title);
//        description.setText(recipe.description);
//
//        ingredientsListView.setAdapter(new ArrayAdapter<>(
//                RecipeActivity.this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                recipe.ingredients
//        ));
//
//        directionsListView.setAdapter(new ArrayAdapter<>(
//                RecipeActivity.this,
//                android.R.layout.simple_list_item_1,
//                android.R.id.text1,
//                recipe.directions
//        ));

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableList);

        // preparing list data
        prepareListData(recipe.description, recipe.ingredients, recipe.directions);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareListData(String description, String[] ingredients, String[] directions) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Description");
        listDataHeader.add("Ingredients");
        listDataHeader.add("Directions");

        List<String> descriptionList = new ArrayList<>();
        descriptionList.add(description);

        List<String> ingredientsList = Arrays.asList(ingredients);
        List<String> directionsList = Arrays.asList(directions);

        listDataChild.put(listDataHeader.get(0), descriptionList);
        listDataChild.put(listDataHeader.get(1), ingredientsList);
        listDataChild.put(listDataHeader.get(2), directionsList);

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
