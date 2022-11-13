package org.n01472825.appointmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnDatePicker;
    Button btnStartTimePicker;
    Button btnEndTimePicker;
    Button btnSubmit;

    private int myYearStart, myMonthStart, myDayStart, myHourStart, myMinuteStart;
    private int myYearStartCache, myMonthStartCache, myDayStartCache, myHourStartCache, myMinuteStartCache;

    private int myHourEnd, myMinuteEnd;
    private int myHourEndCache, myMinuteEndCache;

    private String startTime, endTime, eventDate;
    TextView deatilsView;
    CheckBox checkBox;
    EditText eventTitle, eventDescription, eventAttendees;

    boolean eventSetFlag = false;

    Calendar calStart = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    Calendar calEnd = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnStartTimePicker = (Button) findViewById(R.id.btn_timeStart);
        btnEndTimePicker = (Button) findViewById(R.id.btn_timeEnd);
        btnSubmit = (Button) findViewById(R.id.submit);

        deatilsView = (TextView) findViewById(R.id.params);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        eventTitle = (EditText) findViewById(R.id.eventName);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        eventAttendees = (EditText) findViewById(R.id.invites);

        btnDatePicker.setOnClickListener(this);
        btnStartTimePicker.setOnClickListener(this);
        btnEndTimePicker.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //deatilsView.setText("Your event details : ");
        if (view == btnDatePicker){
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            myYearStart = c.get(Calendar.YEAR);
            myMonthStart = c.get(Calendar.MONTH);
            myDayStart = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //String resultText = deatilsView.getText().toString();
                            //deatilsView.setText(resultText + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            eventDate = "Date : " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            myDayStartCache = dayOfMonth;
                            myMinuteStartCache = monthOfYear+1;
                            myYearStartCache = year;
                        }
                    }, myYearStart, myMonthStart, myDayStart);
            datePickerDialog.show();
            //showDetails();
        }
        if (view == btnStartTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            myHourStart = c.get(Calendar.HOUR_OF_DAY);
            myMinuteStart = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            //String resultText = deatilsView.getText().toString();
                            //deatilsView.setText(resultText + hourOfDay + ":" + minute);
                            startTime = "\nStarts at : "+hourOfDay + ":" + minute;
                            myHourStartCache = hourOfDay;
                            myMinuteStartCache = minute;
                        }
                    }, myHourStart, myMinuteStart, false);
            timePickerDialog.show();
            //showDetails();
        }
        if (view == btnEndTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            myHourEnd = c.get(Calendar.HOUR_OF_DAY);
            myMinuteEnd = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            //String resultText = deatilsView.getText().toString();
                            //deatilsView.setText(resultText + hourOfDay + ":" + minute);
                            endTime = "\nEnds at : " + hourOfDay + ":" + minute;
                            myHourEndCache = hourOfDay;
                            myMinuteEndCache = minute;
                        }
                    }, myHourEnd, myMinuteEnd, false);
            timePickerDialog.show();
            //showDetails();
        }
        if(view == btnSubmit){
            showDetails();
        }
    }

    private void showDetails() {
        if (checkBox.isChecked() && eventDate!=null){
           // String resultText = deatilsView.getText().toString();
            List<String> attendees = new ArrayList<String>();
            attendees = Arrays.asList(eventAttendees.getText().toString().split(","));
            deatilsView.setText("Event title is : "+eventTitle.getText().toString()+"\nEvent Description is : "+eventDescription.getText()+"\nYour event is at : " + eventDate + "\nThe event is scheduled for whole day"+"\nThe people attending are:");
            for (int i = 0; i < attendees.size(); i++) {
                deatilsView.setText(deatilsView.getText().toString() + "\n" + attendees.get(i));
            }

        }
        else if(!checkBox.isChecked() && startTime!=null && endTime!=null){
            deatilsView.setText("Event title is : "+eventTitle.getText().toString()+"\nEvent Description is : "+eventDescription.getText()+"\nYour event is at : " + eventDate + startTime + endTime+"\nThe people attending are:");
            List<String> attendees = new ArrayList<String>();
            attendees = Arrays.asList(eventAttendees.getText().toString().split(","));

            for (int i = 0; i < attendees.size(); i++) {
                deatilsView.setText(deatilsView.getText().toString() + "\n"+ attendees.get(i));
            }
        }


        calStart.set(myYearStartCache,myMonthStartCache,myDayStartCache,myHourStartCache,myMinuteStartCache);
        calEnd.set(myYearStartCache,myMonthStartCache,myDayStartCache,myHourEndCache,myMinuteEndCache);
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, eventTitle.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDescription.getText().toString());
        if(checkBox.isChecked()){
            intent.putExtra(CalendarContract.Events.ALL_DAY, checkBox.isChecked());
            intent.putExtra(CalendarContract.Events.DURATION, "PT1D");
        }
        else{
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,calStart.getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,calEnd.getTimeInMillis());
        }
        intent.putExtra(Intent.EXTRA_EMAIL, eventAttendees.getText().toString());

//        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//        intent.putExtra("title", eventTitle.getText().toString());
//        intent.putExtra("description", eventDescription.getText().toString());
//        intent.putExtra(CalendarContract.Events.ALL_DAY, checkBox.isChecked());
//        calStart.set(myYearStart,myMonthStart,myDayStart,myHourStart,myMinuteStart);
//        calEnd.set(myYearStart,myMonthStart,myDayStart,myHourEnd,myMinuteEnd);
//        intent.putExtra("beginTime", calStart.getTimeInMillis());
//        intent.putExtra("endTime", calEnd.getTimeInMillis());
//        startActivity(intent);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show();

        }
    }


}
