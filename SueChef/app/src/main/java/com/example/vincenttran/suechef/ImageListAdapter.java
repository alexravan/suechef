package com.example.vincenttran.suechef;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.vincenttran.suechef.Recipe;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by vincenttran on 10/5/16.
 */

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private Recipe[] recipes;

    public ImageListAdapter(Context context, Recipe[] recipes) {
        super(context, R.layout.grid_item_layout, recipes);

        this.context = context;
        this.recipes = recipes;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        }

        Picasso
                .with(context)
                .load(recipes[position].imgUrl)
                .fit() // will explain later
                .into( ((ImageView) ((ViewGroup) convertView).getChildAt(0)));

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