package com.example.vincenttran.suechef;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.vincenttran.suechef.Recipe;

class ImageListAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    private final LayoutInflater inflater;

    private final Recipe[] recipes;

    public ImageListAdapter(Context context, Recipe[] recipes) {
        super(context, R.layout.grid_item_layout, recipes);

        this.context = context;
        this.recipes = recipes;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        }

        ImageView img = ((ImageView) ((ViewGroup) convertView).getChildAt(0));
        img.setAdjustViewBounds(true);

        Picasso
                .with(context)
                .load(recipes[position].imgUrl)
                .fit() // will explain later
                .into(img);

        TextView recipeName = (TextView) (((ViewGroup) convertView).getChildAt(1));
        recipeName.setText(recipes[position].title);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipeActivity.class);
                intent.putExtra("recipe", recipes[position]);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}