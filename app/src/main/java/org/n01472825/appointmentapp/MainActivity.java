package org.n01472825.appointmentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnDatePicker;
    Button btnStartTimePicker;
    Button btnEndTimePicker;
    Button btnSubmit;
    Button btnCamera;

    private int myYearStart, myMonthStart, myDayStart, myHourStart, myMinuteStart;
    private int myYearStartCache, myMonthStartCache, myDayStartCache, myHourStartCache, myMinuteStartCache;

    private int myHourEnd, myMinuteEnd;
    private int myHourEndCache, myMinuteEndCache;

    private String startTime, endTime, eventDate;
    TextView deatilsView;
    CheckBox checkBox;
    EditText eventTitle, eventDescription, eventAttendees;

    Calendar calStart = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    Calendar calEnd = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

    Uri imageUri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnStartTimePicker = (Button) findViewById(R.id.btn_timeStart);
        btnEndTimePicker = (Button) findViewById(R.id.btn_timeEnd);
        btnSubmit = (Button) findViewById(R.id.submit);
        btnCamera = (Button) findViewById(R.id.camera);

        //deatilsView = (TextView) findViewById(R.id.params);
        checkBox = (CheckBox) findViewById(R.id.checkBox);

        eventTitle = (EditText) findViewById(R.id.eventName);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        eventAttendees = (EditText) findViewById(R.id.invites);

        btnDatePicker.setOnClickListener(this);
        btnStartTimePicker.setOnClickListener(this);
        btnEndTimePicker.setOnClickListener(this);
        checkBox.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
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

        if (view == btnCamera){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.CAMERA)==
                        PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                PackageManager.PERMISSION_DENIED){
                    //permission not enabled, request permission
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE);
                }
                else{
                    openCamera();
                }
            }
            else{
                openCamera();
            }
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"New Picture from my camera app");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, "Captured image is stored to gallery", Toast.LENGTH_SHORT);
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

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this, "Please fill all the fields correctly", Toast.LENGTH_SHORT).show();

        }
    }


    public void onSendMessage(View view) {
        EditText messageView = (EditText) findViewById(R.id.message);
        String messageText = messageView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Message sent from my messenger app");
        intent.putExtra(Intent.EXTRA_TEXT, messageText);
        startActivity(intent);
    }
}