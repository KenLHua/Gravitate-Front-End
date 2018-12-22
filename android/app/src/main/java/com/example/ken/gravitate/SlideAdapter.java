package com.example.ken.gravitate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ken.gravitate.Account.LoginActivity;

public class SlideAdapter extends PagerAdapter {

    // Field variables
    Context context;
    LayoutInflater inflater;
    // The slide in images
    public int[] lst_images = {
            R.drawable.welcome_screen_car_art,
            R.drawable.card_background_airplane,
            R.drawable.welcome_screen_rivalry_art
    };
    // Titles for the screens
    public String[] lst_title = {
            "Discover Rides",
            "Choose Airport",
            "Match Users"
    };
    // Screen descriptions
    public String[] lst_description = {
            "Discover thousands of rides to your destination.",
            "Choose your card_background_airplane flight time and destination.",
            "We will match you with like-minded people."
    };
    // move the dots for each screen
    public int[] lst_dots = {
            R.drawable.listdots1,
            R.drawable.listdots2,
            R.drawable.listdots3
    };
    // Set background at white for all cards
    public int[] lst_backgroundcolor = {
            Color.rgb(255,255,255),
            Color.rgb(255,255,255),
            Color.rgb(255,255,255)
    };
    public SlideAdapter (Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.slide, container, false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        TextView texttitle = (TextView) view.findViewById((R.id.txttitle));
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        ImageView dots = (ImageView) view.findViewById((R.id.dotimg));

        layoutslide.setBackgroundColor((lst_backgroundcolor[position]));
        imgslide.setImageResource(lst_images[position]);
        texttitle.setText(lst_title[position]);
        description.setText(lst_description[position]);
        dots.setImageResource(lst_dots[position]);

        Button contButton = (Button) view.findViewById(R.id.NextButton);
        if(position != lst_title.length-1){
            contButton.setVisibility(View.GONE);
        }

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext() , LoginActivity.class);
                view.getContext().startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(LinearLayout) o);
    }
}