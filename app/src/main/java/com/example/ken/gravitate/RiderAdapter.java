package com.example.ken.gravitate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//class to iterate the list of cards and put them on the screen
public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.myViewHolder> {

    Context context;
    List<Rider> rider_list;
    public List<myViewHolder> cards = new ArrayList<myViewHolder>();
    int profile;

    private OnRiderClickListener mlistener;

    public interface OnRiderClickListener{
        void onRiderClick(int position);
    }
    public void setOnRiderClickListener(OnRiderClickListener listener){
        mlistener = listener;
    }

    public RiderAdapter(Context context, List<Rider> rider_list) {
        this.context = context;
        this.rider_list = rider_list;
    }

    //create each Card
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.rider, viewGroup, false);
        myViewHolder tempViewHolder = new myViewHolder(v);
        cards.add(tempViewHolder);
        Log.d("cards", cards.size()+"");

        return tempViewHolder;
    }

    //set the correct images and text for each Card
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int i) {
        holder.profile_photo.setImageResource(rider_list.get(i).getProfile_photo());
        holder.fullname.setText(rider_list.get(i).getFullname());
        holder.email.setText(rider_list.get(i).getEmail());
    }

    //get number of cards
    @Override
    public int getItemCount() {
        return rider_list.size();
    }


    //The class that gets the reference to each individual element of the cards
    public class myViewHolder extends  RecyclerView.ViewHolder {

        public CircleImageView profile_photo;
        TextView fullname, email;

        public myViewHolder(View itemView) {
            super(itemView);
            profile_photo = itemView.findViewById(R.id.event_page_photo);

            fullname = itemView.findViewById(R.id.rider_name);

            email = itemView.findViewById(R.id.rider_email);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mlistener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mlistener.onRiderClick(position);
                        }
                    }
                }
            });
        }
    }



}