package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LockedJob extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ParseObject obj;
    TextView id,ngeo,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t1U,t2N,t2U,t3N,t3U,t1Time,t2Time,t3Time;
    EditText dateAndTime,addressEditText;
    LinearLayout l1,l2,l3;
    Button t1No,t2No,t3No,t1view,t2view,t3view;
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
        ngeo = findViewById(R.id.negoJP);
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

        //Edit Text
        dateAndTime = findViewById(R.id.dateAndTime);
        addressEditText = findViewById(R.id.addressE);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Buttons
        t1No = findViewById(R.id.t1No);
        t2No = findViewById(R.id.t2No);
        t3No = findViewById(R.id.t3No);

        t1view = findViewById(R.id.tview1);
        t2view = findViewById(R.id.tview2);
        t3view = findViewById(R.id.tview3);
        //Map
        map = new HashMap<>();
    }

    @SuppressLint("SetTextI18n")
    private void set() {
        
        if(obj.getList("applied")!=null){
            if(obj.getList("applied").size()!=0) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Something is very wrong")
                        .setMessage("Please Contact Tanzim")
                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                finish();
                            }
                        }).show();
            }
        }

        id.setText("ID: "+ obj.getObjectId());
        ngeo.setText("Nego: "+ obj.getBoolean("negotiable"));

        name.setText(""+obj.getParseObject("createdBy").getString("guardianName"));
        salary.setText("Salary: "+ obj.get("salary").toString());
        location.setText("Location: "+ obj.getString("location"));
        stdNumber.setText("Number: "+ obj.get("numberOfStudents").toString());
        sClass.setText("Class: "+ obj.getString("class1")+","+ obj.getString("class2"));
        sub.setText("Subject1: "+ obj.getString("subject1")+"\nSubject2: "+ obj.getString("subject2"));
        address.setText("Address: "+ obj.getString("address"));
        dateAndTime.setText(obj.getString("gTimeDate"));
        // Setting visibility to false
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        t1Time.setVisibility(View.GONE);
        t2Time.setVisibility(View.GONE);
        t3Time.setVisibility(View.GONE);

        t1view.setVisibility(View.GONE);
        t2view.setVisibility(View.GONE);
        t3view.setVisibility(View.GONE);
        //-------------------------------------

        requested = obj.getList("requested");
        if(requested!=null && !requested.isEmpty()){
            for (int i = 0; i < requested.size(); i++) {
                if(i==0){
                    t1view.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    t1N.setText(requested.get(0).getString("fullName"));
                    t1Time.setVisibility(View.VISIBLE);

                    if(requested.get(0).getBoolean("banLock"))
                        t1N.setTextColor(Color.RED);

                    map.put("l1",requested.get(i).getObjectId());
                }else if(i==1){
                    t2view.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    t2Time.setVisibility(View.VISIBLE);

                    t2N.setText(requested.get(1).getString("fullName"));

                    if(requested.get(1).getBoolean("banLock"))
                        t2N.setTextColor(Color.RED);

                    map.put("l2",requested.get(i).getObjectId());
                }else{
                    t3view.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(2).getString("fullName"));
                    t3Time.setVisibility(View.VISIBLE);

                    if(requested.get(2).getBoolean("banLock"))
                        t3N.setTextColor(Color.RED);

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
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").get("username")));
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

        intent.setData(Uri.parse("tel:"+temp.getString("username")));
        startActivity(intent);
    }

    public void notGoing(final View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Not Going? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id;
                List<ParseObject> requested = obj.getList("requested");
                if (view.getId()==R.id.t1No){
                    interviewTime.set(0,"");
                    id = map.get("l1");
                    callCloudNotGoing(id,requested,l1,t1Time,t1view);
                }else if(view.getId()==R.id.t2No){
                    interviewTime.set(1,"");
                    id = map.get("l2");
                    callCloudNotGoing(id,requested, l2, t2Time,t2view);
                }else{
                    interviewTime.set(2,"");
                    id = map.get("l3");
                    callCloudNotGoing(id,requested, l3, t3Time,t3view);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialogBuilder.show();



    }

    private void callCloudNotGoing(String id, List<ParseObject> requested, final LinearLayout l, final TextView time, final Button view) {
        String username = null;
        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                username = requested.get(i).getString("username");
            }
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("objectId", obj.getObjectId());
        params.put("interviewTime",interviewTime);
        ParseCloud.callFunctionInBackground("notGoing", params, new FunctionCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null){
                    obj = object;
                    view.setVisibility(View.GONE);
                    l.setVisibility(View.GONE);
                    time.setVisibility(View.GONE);
                }else{
                    Toast.makeText(LockedJob.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void submit(View view) {
        if(year==-1)
            Toast.makeText(this, "Set date", Toast.LENGTH_SHORT).show();
        else if(l1.getVisibility()==View.VISIBLE && t1Time.getText().toString().equals("T1 Time"))
            Toast.makeText(this, "Set Teacher time", Toast.LENGTH_SHORT).show();
        else if(l2.getVisibility()==View.VISIBLE && t2Time.getText().toString().equals("T2 Time"))
            Toast.makeText(this, "Set Teacher time", Toast.LENGTH_SHORT).show();
        else if(l3.getVisibility()==View.VISIBLE && t3Time.getText().toString().equals("T3 Time"))
            Toast.makeText(this, "Set Teacher time", Toast.LENGTH_SHORT).show();
        else{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Submit? Are you sure?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // copying the ArrayList zoo to the ArrayList list
                    List<String> tempArr = new ArrayList<>(interviewTime);
                    tempArr.removeAll(Collections.singletonList(""));

                    HashMap<String,Object> params = new HashMap<>();
                    params.put("id",obj.getObjectId());
                    params.put("year",year);
                    params.put("month",month);
                    params.put("day",dayOfMonth);
                    params.put("interviewTime",tempArr);

                    if(dateAndTime.getText().length() != 0)
                        params.put("gTimeDate",dateAndTime.getText().toString());

                    ParseCloud.callFunctionInBackground("lockedJobSubmit", params, new FunctionCallback<Boolean>() {
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
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alertDialogBuilder.show();
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
        ((TextView)findViewById(R.id.dateView)).setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }

    public void saveChanges(View view) {
        if(dateAndTime.getText().length() != 0)
            obj.put("gTimeDate",dateAndTime.getText().toString());
        if(addressEditText.getText().length() != 0)
            obj.put("address",addressEditText.getText().toString());

        obj.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(LockedJob.this, "Done", Toast.LENGTH_SHORT).show();
                    address.setText("Address: "+ obj.getString("address"));
                }else{
                    Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void rePost(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Repost? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String,Object> params = new HashMap<>();
                params.put("id",obj.getObjectId());
                params.put("num",1);
                ParseCloud.callFunctionInBackground("repost", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                    if(e==null){
                        int pos = getIntent().getIntExtra("pos",-1);
                        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                        finish();
                    }else{
                        Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialogBuilder.show();

    }

    public void delete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete, Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String,Object> params = new HashMap<>();
                params.put("id",obj.getObjectId());
                params.put("num",1);
                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if(e==null){
                            int pos = getIntent().getIntExtra("pos",-1);
                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                            finish();
                        }else{
                            Toast.makeText(LockedJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialogBuilder.show();

    }

    public void seeDetails(View view) {

        String id;

        List<ParseObject> requested = obj.getList("requested");

        if (view.getId()==R.id.tview1){
            id = map.get("l1");
        }else if(view.getId()==R.id.tview2){
            id = map.get("l2");
        }else{
            id = map.get("l3");
        }

        ParseObject tObj = null;
        for (int i = 0; i < requested.size(); i++) {
            if(id.equals(requested.get(i).getObjectId())){
                tObj = requested.get(i);
            }
        }
        List<String> times = tObj.getList("interviewTimes");
        if(times!=null && !times.isEmpty()){
            String tempTime = "";
            for (int i = 0; i < times.size(); i++) {
                tempTime += times.get(i) + "\n";
            }
            // create an alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Info");
            // set the custom layout
            final View customLayout = getLayoutInflater().inflate(R.layout.custom_locked_layout, null);

            ((TextView)customLayout.findViewById(R.id.time)).setText(tempTime);
            builder.setView(customLayout);
            builder.show();
        }else {
            Toast.makeText(this, "No Time", Toast.LENGTH_SHORT).show();
        }
    }
}
