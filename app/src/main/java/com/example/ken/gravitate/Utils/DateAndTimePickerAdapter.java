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


    private DatePickerDialog.OnDateSetListener mDateListener;
    private TimePickerDialog.OnTimeSetListener mTimeListener;
    private TimePickerDialog mTimePicker;
    private Context mContext;
    private Calendar myCal;
    private TextView mTextView;
    private TextView mRelatedView;
    private int mRelatedOffset;

    public DateAndTimePickerAdapter(Calendar cal, TextView textView, android.content.Context context){
        myCal = cal;
        mTextView = textView;
        mContext = context;
        mDateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int localYear, int monthOfYear, int dayOfMonth) {
                Log.d("onDateSet", "called");
                myCal.set(Calendar.YEAR, localYear);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCal.set(Calendar.MINUTE, minute);
                        updateLabel(mTextView);
                    }
                };
                mTimePicker = new TimePickerDialog(mContext, mTimeListener,
                        myCal.get(Calendar.HOUR_OF_DAY), myCal.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        };
    }

    public DateAndTimePickerAdapter(Calendar cal, TextView textView, final TextView relatedView, int hourOffset, android.content.Context context){
        myCal = cal;
        mTextView = textView;
        mContext = context;
        mRelatedView = relatedView;
        mRelatedOffset = hourOffset;
        mDateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int localYear, int monthOfYear, int dayOfMonth) {
                Log.d("onDateSet", "called");
                myCal.set(Calendar.YEAR, localYear);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCal.set(Calendar.MINUTE, minute);
                        // Check if the inputted time is at least |mRelatedOffset| hours in the future
                        // Say if you offset the hours in -4 hours, you'd have to do something immediately
                        // If you offset by 4 hour, you'd have to do something in 8 hours
                        if((myCal.getTimeInMillis()) < Calendar.getInstance().getTimeInMillis()+(Math.abs(mRelatedOffset)*HOUR_IN_MILLISECONDS)){
                            Toast.makeText(mContext, "Error: Input a Departure Time more than 4 hours away", Toast.LENGTH_LONG).show();
                            return;
                        }
                        updateLabel(mTextView);
                        updateRelatedLabel(mRelatedView, Calendar.HOUR, mRelatedOffset);
                    }
                };
                mTimePicker = new TimePickerDialog(mContext, mTimeListener,
                        myCal.get(Calendar.HOUR_OF_DAY), myCal.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        };
    }



    public DatePickerDialog.OnDateSetListener getDateListener(){
        return mDateListener;
    }



    private void updateLabel(TextView textView) {
        String myFormat = "MM/dd/yyyy hh:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(sdf.format(myCal.getTime()));
    }

    // Update a view that is dependent on another's time
    private void updateRelatedLabel(TextView relatedView, int timeUnit, int timeOffset){
        String myFormat = "MM/dd/yyyy hh:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Calendar relatedCalendar = Calendar.getInstance();
        relatedCalendar.setTime(myCal.getTime());
        relatedCalendar.add(Calendar.HOUR, timeOffset);
        relatedView.setText(sdf.format(relatedCalendar.getTime()));

    }

}
