package com.example.ken.gravitate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ken.gravitate.Utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class forcematch2 extends AppCompatActivity {

    private TextInputEditText input1;
    private TextInputEditText input2;
    private Button confirmButton;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.force_match_two);

        mContext = this;

        input1 = findViewById(R.id.INPUTTEXT1);
        input2 = findViewById(R.id.INPUTTEXT2);
        confirmButton = findViewById(R.id.confirmButton);



        final JSONObject forcematch = new JSONObject();
        final List<String> rideIDs = new ArrayList<>();



        confirmButton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {

                    Log.d("RIDEREQUEST1", input1.getText().toString());
                    Log.d("RIDEREQUEST2",input2.getText().toString());

                    rideIDs.add(input1.getText().toString());
                    rideIDs.add(input2.getText().toString());
                    if(rideIDs.size() == 2) {

                        try {
                            forcematch.put("operationMode", "two");
                            forcematch.put("rideRequestIds", rideIDs);
                            APIUtils.postForceMatch(mContext, forcematch);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });



    }
}
