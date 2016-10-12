package com.example.vincenttran.suechef;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import com.example.vincenttran.suechef.Recipe;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class RecipeActivity extends AppCompatActivity implements RecognitionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ImageView img = (ImageView) findViewById(R.id.recipeDetailImg);
        TextView title = (TextView) findViewById(R.id.recipeDetailTitle);
        TextView description = (TextView) findViewById(R.id.recipeDetailDesc);
        ListView ingredientsListView = (ListView) findViewById(R.id.ingredientsListView);
        ListView directionsListView = (ListView) findViewById(R.id.directionsListView);

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

        title.setText(recipe.title);
        description.setText(recipe.description);

        ingredientsListView.setAdapter(new ArrayAdapter<>(
                RecipeActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                recipe.ingredients
        ));

        directionsListView.setAdapter(new ArrayAdapter<>(
                RecipeActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                recipe.directions
        ));

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
