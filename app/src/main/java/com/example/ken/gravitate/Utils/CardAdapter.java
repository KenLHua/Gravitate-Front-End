package com.example.ken.gravitate.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ken.gravitate.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//class to iterate the list of cards and put them on the screen
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHolder> {

    Context context;
    List<Card> card_list;

    private OnCardClickListener mlistener;

    public interface OnCardClickListener{
        void onCardClick(int position);
    }
    public void setOnCardClickListener(OnCardClickListener listener){
        mlistener = listener;
    }

    public CardAdapter(Context context, List<Card> card_list) {
        this.context = context;
        this.card_list = card_list;
    }

    //create each Card
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.card, viewGroup, false);
        return new myViewHolder(v);
    }

    //set the correct images and text for each Card
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int i) {
        holder.background_img.setImageResource(card_list.get(i).getBackground());
        holder.profile_photo.setImageResource(card_list.get(i).getProfilePhoto());
        holder.card_dest.setText(card_list.get(i).getDestName());
        holder.card_time.setText(card_list.get(i).getDestTime());
    }

    //get number of cards
    @Override
    public int getItemCount() {
        return card_list.size();
    }


    //The class that gets the reference to each individual element of the cards
    public class myViewHolder extends  RecyclerView.ViewHolder {

        ImageView background_img;
        CircleImageView profile_photo;
        TextView card_dest, card_time;

        public myViewHolder(View itemView) {
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



}