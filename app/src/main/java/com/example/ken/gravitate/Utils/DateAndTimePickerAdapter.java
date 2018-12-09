package com.example.ken.gravitate.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTimePickerAdapter {
    private int HOUR_IN_MILLISECONDS = 3600000;
    private int MINUTE_IN_MILLISECONDS = 60000;
    private int SECOND_IN_MILLISECONDS = 1000;


    // Field variables
    private DatePickerDialog.OnDateSetListener mDateListener;
    private TimePickerDialog.OnTimeSetListener mTimeListener;
    private TimePickerDialog mTimePicker;
    private Context mContext;
    private Calendar myCal;
    private TextView mTextView;


    // Date Picker that helps users choose a date
    public DateAndTimePickerAdapter(Calendar cal, TextView textView, android.content.Context context, final boolean chooseTime){
        // Boolean to choose 24:00 time along with date
        if( chooseTime) {
            myCal = cal;
            mTextView = textView;
            mContext = context;
            // Behavior for date picker
            mDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int localYear, int monthOfYear, int dayOfMonth) {
                    Log.d("onDateSet", "called");
                    myCal.set(Calendar.YEAR, localYear);
                    myCal.set(Calendar.MONTH, monthOfYear);
                    myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    // Behavior for time
                    mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            myCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            myCal.set(Calendar.MINUTE, minute);
                            // Check if chosen time is in the future
                            if ((myCal.getTimeInMillis()) < Calendar.getInstance().getTimeInMillis()) {
                                return;
                            }
                            updateLabel(mTextView, chooseTime);
                        }
                    };
                    mTimePicker = new TimePickerDialog(mContext, mTimeListener,
                            myCal.get(Calendar.HOUR_OF_DAY), myCal.get(Calendar.MINUTE), false);
                    mTimePicker.show();
                }
            };
        }
        else {
            // Just have a date picker
            myCal = cal;
            mTextView = textView;
            mContext = context;
            mDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int localYear, int monthOfYear, int dayOfMonth) {
                    Log.d("onDateSet", "called");
                    myCal.set(Calendar.YEAR, localYear);
                    myCal.set(Calendar.MONTH, monthOfYear);
                    myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel(mTextView, chooseTime);
                }
            };
        }
    }



    public DatePickerDialog.OnDateSetListener getDateListener(){
        return mDateListener;
    }



    // Have the user's choice display
    private void updateLabel(TextView textView, boolean chooseTime) {
        if(chooseTime) {
            String myFormat = "MM/dd/yyyy hh:mm a";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            textView.setText(sdf.format(myCal.getTime()));
        }
        else{
            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            textView.setText(sdf.format(myCal.getTime()));
        }
    }


}
