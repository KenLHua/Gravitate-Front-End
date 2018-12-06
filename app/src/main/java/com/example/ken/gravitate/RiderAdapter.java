package com.example.ken.gravitate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ken.gravitate.Models.Rider;
import com.example.ken.gravitate.Utils.APIUtils;
import com.example.ken.gravitate.Utils.AuthSingleton;
import com.example.ken.gravitate.Utils.DownloadImageTask;
import com.example.ken.gravitate.Utils.VolleyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//class to iterate the list of cards and put them on the screen
public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderViewHolder> {

    Context context;
    List<Rider> rider_list;
    public List<RiderViewHolder> cards = new ArrayList<RiderViewHolder>();
    private FirebaseUser currUser = AuthSingleton.getInstance().getCurrentUser();
    private FirebaseUser partnerUser;
    public boolean stillPending;
    private DocumentReference mOrbitRef;
    public ArrayList<String> profileImages;

    private OnRiderClickListener mlistener;

    public interface OnRiderClickListener{
        void onRiderClick(int position);
    }
    public void setOnRiderClickListener(OnRiderClickListener listener){
        mlistener = listener;
    }

    public RiderAdapter(Context context, DocumentReference orbitRef, List<Rider> rider_list) {
        this.context = context;
        this.mOrbitRef = orbitRef;
        this.rider_list = rider_list;
    }
    public void setProfileImages(ArrayList<String> profileImages){
        this.profileImages = profileImages;
    }
    //create each Card
    @NonNull
    @Override
    public RiderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(!stillPending) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.rider, viewGroup, false);
            RiderViewHolder tempViewHolder = new RiderViewHolder(v, this.mOrbitRef, this.context);
            cards.add(tempViewHolder);
            Log.d("cards", cards.size() + "");

            return tempViewHolder;
        }
        return null;
    }

    //set the correct images and text for each Card
    @Override
    public void onBindViewHolder(@NonNull RiderViewHolder holder, int i) {
    }

    //get number of cards
    @Override
    public int getItemCount() {
        return rider_list.size();
    }


    //The class that gets the reference to each individual element of the cards
    public class RiderViewHolder extends  RecyclerView.ViewHolder {

        public CircleImageView profile_photo;
        TextView fullname, email, phone_number;
        Context mContext;

        public RiderViewHolder(View itemView, DocumentReference orbitRef, Context context) {
            super(itemView);
            mContext = context;

            profile_photo = itemView.findViewById(R.id.profile_photo);
            fullname = itemView.findViewById(R.id.rider_name);
            email = itemView.findViewById(R.id.rider_email);
            phone_number = itemView.findViewById(R.id.phone_number);
            orbitRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    HashMap<String, Object> userTicketPairs = (HashMap<String, Object>) task.getResult().get("userTicketPairs");
                    Object[] pairedUserIDs = userTicketPairs.keySet().toArray();
                    for ( Object currID : pairedUserIDs){
                        String currIDString = (String) currID;
                        if(currIDString.equals(currUser.getUid())){
                            // Do nothing
                        }
                        else{
                            String request_url = APIUtils.getUserURL((String) currID);
                            Log.d("taggyBoi", (String) currID);
                            APIUtils.getUser(mContext, request_url,
                                    new VolleyCallback() {
                                        @Override
                                        public void onSuccessResponse(String result) {
                                            try {
                                                JSONObject response = new JSONObject(result);
                                                fullname.setText(response.getString("display_name"));
                                                email.setText(response.getString("email"));
                                                phone_number.setText(response.getString("phone_number"));
                                                new DownloadImageTask(profile_photo).execute(response.getString("photo_url"));

                                            } catch (JSONException e ) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    }

                }
            });

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