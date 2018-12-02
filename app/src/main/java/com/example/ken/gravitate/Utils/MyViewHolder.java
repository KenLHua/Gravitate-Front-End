package com.example.ken.gravitate.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends  RecyclerView.ViewHolder {

    public ImageView background_img;
    public CircleImageView profile_photo;
    public TextView card_dest, card_time;
    private CardAdapter.OnCardClickListener mlistener;

    public MyViewHolder(View itemView) {
        super(itemView);
        profile_photo = itemView.findViewById(R.id.profile_image);
        background_img = itemView.findViewById(R.id.card_back);
        card_dest = itemView.findViewById(R.id.card_dest);
        card_time = itemView.findViewById(R.id.card_date);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mlistener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mlistener.onCardClick(position);
                    }
                }
            }
        });
    }
}