package com.example.vincenttran.suechef;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import com.example.vincenttran.suechef.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeActivity extends AppCompatActivity implements RecognitionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        ImageView img = (ImageView) findViewById(R.id.recipeDetailImg);
        Picasso
                .with(this)
                .load(recipe.imgUrl)
//                .fit()
                .into(img);
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
