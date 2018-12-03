package com.example.ken.gravitate.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends  RecyclerView.ViewHolder {
    private View view;
    public ImageView background_img;
    public CircleImageView profile_photo;
    public TextView card_dest, card_time;
    private MyViewHolder.ClickListener mClickListener;

    public MyViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        profile_photo = itemView.findViewById(R.id.profile_image);
        background_img = itemView.findViewById(R.id.card_back);
        card_dest = itemView.findViewById(R.id.card_dest);
        card_time = itemView.findViewById(R.id.card_date);

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

    }


    public interface ClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(MyViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
