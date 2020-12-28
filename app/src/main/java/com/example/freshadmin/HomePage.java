package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomePage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        String requiredPermission = Manifest.permission.RECEIVE_SMS;
        int checkVal = this.checkCallingOrSelfPermission(requiredPermission);
        if (checkVal == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(HomePage.this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    1);
        }
    }

    public void lockedJobs(View view) {
        startActivity(new Intent(this,LockedJobsList.class));
    }

    public void interview(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        // start of today
        Date today = cal.getTime();

        // start of tomorrow
        cal.add(Calendar.DAY_OF_MONTH, 1); // add one day to get start of tomorrow
        Date tomorrow = cal.getTime();

        // Your query:
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.whereGreaterThan("interviewDate", today);
        query.whereLessThan("interviewDate", tomorrow);
        query.include("createdBy.sProfile");
        query.include("requested");
        query.whereDoesNotExist("hired");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.isEmpty()){
                        Toast.makeText(HomePage.this, "All done for today", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(HomePage.this, InterviewDayList.class).putParcelableArrayListExtra("objects", (ArrayList<? extends Parcelable>) objects));
                    }
                }else{
                    Toast.makeText(HomePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void afterBan(View view) {
        startActivity(new Intent(this,AfterBanList.class));
    }

    public void logout(View view) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(HomePage.this, "Done", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(HomePage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sVerify(View view) {
        startActivity(new Intent(this,VerSList.class));
    }

    public void tVerify(View view) {
        startActivity(new Intent(this,VerTList.class));
    }

    public void paid(View view) {
        startActivity(new Intent(this,PaidJobsList.class));
    }

    public void interviewCancel(View view) {

    }
}