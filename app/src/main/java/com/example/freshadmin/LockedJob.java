package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class LockedJob extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t1U,t2N,t2U,t3N,t3U,t1Time,t2Time,t3Time;
    EditText dateAndTime;
    LinearLayout l1,l2,l3;
    Button t1No,t2No,t3No;
    HashMap<String,String> map;
    List<String> interviewTime;
    List<ParseObject> requested;
    int year = -1,month,dayOfMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_job);
        init();
        set();
    }

    private void init() {
        obj= getIntent().getParcelableExtra("obj");

        //Text views
        id = findViewById(R.id.jobIdJP);
        name = findViewById(R.id.nameJP);
        salary = findViewById(R.id.salaryJP);
        location = findViewById(R.id.locationJP);
        stdNumber = findViewById(R.id.studentNumberJP);
        sClass = findViewById(R.id.classJP);
        sub = findViewById(R.id.subjectJP);
        curr = findViewById(R.id.curriculumJP);
        address = findViewById(R.id.addressJP);

        //Teacher
        t1N = findViewById(R.id.t1N);
        t1U = findViewById(R.id.t1U);
        t2N = findViewById(R.id.t2N);
        t2U = findViewById(R.id.t2U);
        t3N = findViewById(R.id.t3N);
        t3U = findViewById(R.id.t3U);
        t1Time= findViewById(R.id.t1Time);
        t2Time = findViewById(R.id.t2Time);
        t3Time = findViewById(R.id.t3Time);
        dateAndTime = findViewById(R.id.dateAndTime);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Buttons
        t1No = findViewById(R.id.t1No);
        t2No = findViewById(R.id.t2No);
        t3No = findViewById(R.id.t3No);
        //Map
        map = new HashMap<>();
    }

    @SuppressLint("SetTextI18n")
    private void set() {
        id.setText("ID: "+ obj.getObjectId()); //createdBy.sProfile.guardianName
        name.setText(""+obj.getParseObject("createdBy").getParseObject("sProfile").getString("guardianName"));
        salary.setText("Salary: "+ obj.get("salary").toString());
        location.setText("Location: "+ obj.getString("location"));
        stdNumber.setText("Number: "+ obj.get("numberOfStudents").toString());
        sClass.setText("Class: "+ obj.getString("class1")+","+ obj.getString("class2"));
        sub.setText("Subject1: "+ obj.getString("subject1")+"\nSubject2: "+ obj.getString("subject2"));
        address.setText("Address: "+ obj.getString("address"));

        // Setting visibility to false
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        t1Time.setVisibility(View.GONE);
        t2Time.setVisibility(View.GONE);
        t3Time.setVisibility(View.GONE);
        //-------------------------------------

        requested = obj.getList("requested");
        if(requested!=null && !requested.isEmpty()){
            for (int i = 0; i < requested.size(); i++) {
                if(i==0){
                    l1.setVisibility(View.VISIBLE);
                    t1N.setText(requested.get(0).getString("username"));
                    map.put("l1",requested.get(i).getObjectId());
                }else if(i==1){
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(1).getString("username"));
                    map.put("l2",requested.get(i).getObjectId());
                }else{
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(2).getString("username"));
                    map.put("l3",requested.get(i).getObjectId());
                }
            }
        }



        interviewTime = obj.getList("interviewTime");
        if(interviewTime!=null && !interviewTime.isEmpty()){
            for (int i = 0; i < interviewTime.size(); i++) {
                if(i==0){
                    if(!interviewTime.get(i).equals("")){
                        t1Time.setText(interviewTime.get(i));
                        t1Time.setVisibility(View.VISIBLE);
                    }
                }else if(i==1){
                    if(!interviewTime.get(i).equals("")) {
                        t2Time.setText(interviewTime.get(i));
                        t2Time.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(!interviewTime.get(i).equals("")){
                        t3Time.setText(interviewTime.get(i));
                        t3Time.setVisibility(View.VISIBLE);
                    }
                }
            }
        }else{
            interviewTime = new ArrayList<>(Arrays.asList("","",""));
        }
    }


    public void timeSetter(final View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(LockedJob.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String amPm;
                if (hourOfDay == 12) {
                    amPm = "PM";
                } else if(hourOfDay > 12){
                    amPm = "PM";
                    hourOfDay = hourOfDay-12;
                }else if(hourOfDay == 0){
                    hourOfDay = 12;
                    amPm = "AM";
                } else  {
                    amPm = "AM";
                }

                if (view.getId()==R.id.t1SetTime){
                    t1Time.setVisibility(View.VISIBLE);
                    t1Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);

                    //putting the time array
                    interviewTime.set(0,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);

                    obj.put("interviewTime",interviewTime);

                }else if(view.getId()==R.id.t2SetTime){
                    //Setting text view
                    t2Time.setVisibility(View.VISIBLE);
                    t2Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);

                    //Putting time into array
                    interviewTime.set(1,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);

                    obj.put("interviewTime",interviewTime);
                }else{
                    //Setting text view
                    t3Time.setVisibility(View.VISIBLE);
                    t3Time.setText(String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);
                    //Putting time into array
                    interviewTime.set(2,String.format("%02d:%02d", hourOfDay, minutes) + " "+amPm);

                    obj.put("interviewTime",interviewTime);
                }
            }
        }, 12, 0, false);
        timePickerDialog.show();
    }




    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        List<ParseObject> requested = obj.getList("requested");

        String id;
        if (view.getId()==R.id.t1Call){
            id = map.get("l1");
        }else if(view.getId()==R.id.t2Call){
            id = map.get("l2");
        }else{
            id = map.get("l3");
        }

        ParseObject temp = null;
        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                temp = requested.get(i);
            }
        }

        intent.setData(Uri.parse("tel:"+temp.getString("phone")));
        startActivity(intent);
    }

    public void notGoing(View view) {
        String id;
        //Getting id and Setting corresponding time to ""
        if (view.getId()==R.id.t1No){
            l1.setVisibility(View.GONE);
            t1Time.setVisibility(View.GONE);
            interviewTime.set(0,"");

            id = map.get("l1");
        }else if(view.getId()==R.id.t2No){
            l2.setVisibility(View.GONE);
            t2Time.setVisibility(View.GONE);
            interviewTime.set(1,"");

            id = map.get("l2");
        }else{
            l3.setVisibility(View.GONE);
            t3Time.setVisibility(View.GONE);
            interviewTime.set(2,"");

            id = map.get("l3");
        }

        //Getting the parse object
        int removeIndex=-1;

        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                removeIndex = i;
            }
        }

        //Adding to removed
        obj.add("removed",requested.get(removeIndex));

        //removing from Requested
        requested.remove(removeIndex);
        obj.remove("requested");
        obj.addAll("requested",requested);

        //Modifying interviewTime array
        obj.remove("interviewTime");
        obj.addAll("interviewTime",interviewTime);

        obj.saveEventually();
    }

    public void submit(View view) {
        if(year==-1){
            Toast.makeText(this, "Set date", Toast.LENGTH_SHORT).show();
        }else{
            interviewTime.removeAll(Collections.singletonList(""));

            HashMap<String,Object> params = new HashMap<>();
            params.put("id",obj.getObjectId());
            params.put("year",year);
            params.put("month",month);
            params.put("day",dayOfMonth);
            params.put("interviewTime",interviewTime);

            if(dateAndTime.getText().length() != 0)
                params.put("gTimeDate",dateAndTime.getText().toString());

            ParseCloud.callFunctionInBackground(" ", params, new FunctionCallback<Boolean>() {
                @Override
                public void done(Boolean object, ParseException e) {
                    if(e==null){
                        Toast.makeText(LockedJob.this, "Success", Toast.LENGTH_SHORT).show();
                        int pos = getIntent().getIntExtra("pos",-1);
                        setResult(RESULT_OK,new Intent().putExtra ("pos",pos));
                        finish();
                    }else{
                        Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void setDate(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public void saveChanges(View view) {
        if(dateAndTime.getText().length() != 0)
            obj.put("gTimeDate",dateAndTime.getText().toString());
        obj.saveEventually();
    }

    public void rePost(View view) {
        obj.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(LockedJob.this, "Done", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }

    public void delete(View view) {
        obj.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(LockedJob.this, "Delete Complete", Toast.LENGTH_SHORT).show();
                    int pos = getIntent().getIntExtra("pos",-1);
                    setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                    finish();
                }else{
                    Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
