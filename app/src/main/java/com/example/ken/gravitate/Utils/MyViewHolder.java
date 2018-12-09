package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ken.gravitate.R;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends  RecyclerView.ViewHolder {
    private View view;
    public ImageView background_img;
    public CircleImageView profile_photo;
    public TextView card_dest, card_time, card_pending;
    public Context context;
    public boolean stillPending;
    public DocumentReference orbitRef;
    public List<String> profileImages;
    public String requestPath;

    private MyViewHolder.ClickListener mClickListener;

    public MyViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        profile_photo = itemView.findViewById(R.id.profile_image1);
        background_img = itemView.findViewById(R.id.card_back);
        card_dest = itemView.findViewById(R.id.card_dest);
        card_time = itemView.findViewById(R.id.card_date);
        card_pending = itemView.findViewById(R.id.card_pending);

        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
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
