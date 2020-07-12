package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PaymentOverdue extends AppCompatActivity {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t2N,t3N,t2Time,t3Time;
    LinearLayout l2,l3;
    Button ban,t2hire,t3hire;
    List<ParseObject> requested;
    List<String> interviewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        init();
        set();
    }

    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //Text views
        id = findViewById(R.id.jobIdPO);
        name = findViewById(R.id.namePO);
        salary = findViewById(R.id.salaryPO);
        location = findViewById(R.id.locationPO);
        stdNumber = findViewById(R.id.studentNumberPO);
        sClass = findViewById(R.id.classPO);
        sub = findViewById(R.id.subjectPO);
        curr = findViewById(R.id.curriculumPO);
        address = findViewById(R.id.addressPO);

        //Teacher
        t1N = findViewById(R.id.t1N);

        t2N = findViewById(R.id.t2N);
        t3N = findViewById(R.id.t3N);
        t2Time = findViewById(R.id.t2Time);
        t3Time = findViewById(R.id.t3Time);

        //linear layouts
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Buttons
        ban = findViewById(R.id.ban);
        t2hire = findViewById(R.id.t2Hire);
        t3hire = findViewById(R.id.t3Hire);

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


        t1N.setText(obj.getParseObject("hired").get("username")+"");
        if(obj.getParseObject("hired").getBoolean("banLock")){
            ban.setEnabled(false);
        }

        // Setting visibility to false
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);

        t2Time.setVisibility(View.GONE);
        t3Time.setVisibility(View.GONE);
        //-------------------------------------

        requested = obj.getList("requested");
        if(requested!=null && !requested.isEmpty()){
            for (int i = 0; i < requested.size(); i++) {
                if(i==0){
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(0).getString("username"));
                }else if(i==1){
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(1).getString("username"));

                }
            }
        }

        interviewTime = obj.getList("interviewTime");
        if(interviewTime!=null && !interviewTime.isEmpty()){
            for (int i = 0; i < interviewTime.size(); i++) {
                if(i==0){
                        t2Time.setText(interviewTime.get(i));
                        t2Time.setVisibility(View.VISIBLE);
                }else if(i==1){
                        t3Time.setText(interviewTime.get(i));
                        t3Time.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    public void ban(View view) {
        HashMap<String, String> params = new HashMap();
        params.put("profileId",obj.getParseObject("hired").getObjectId());
        params.put("jobId",obj.getObjectId());
        ParseCloud.callFunctionInBackground("banTeacher", params, new FunctionCallback<String>() {
            @Override
            public void done(String str, ParseException e) {
                if (e == null) {
                    Toast.makeText(PaymentOverdue.this, "Teacher was banned", Toast.LENGTH_SHORT).show();
                    ban.setEnabled(false);
                }else{
                    Toast.makeText(PaymentOverdue.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
    }

    public void hire(View view) {
         if(view.getId()==R.id.t2Hire){
            t2hire.setTextColor(Color.parseColor("#D81B60"));

            t3hire.setTextColor(Color.parseColor("#050505"));
        }else{
            t3hire.setTextColor(Color.parseColor("#D81B60"));

            t2hire.setTextColor(Color.parseColor("#050505"));
        }

    }

    public void submit(View view) {
        //        //getting requested array index
        int colorH2 = t2hire.getTextColors().getDefaultColor();
        int colorH3 = t3hire.getTextColors().getDefaultColor();



        int i=-1;
        if(colorH2==Color.parseColor("#D81B60")){
            i=0;
        }else if(colorH3==Color.parseColor("#D81B60")){
            i=1;
        }

        if(ban.isEnabled()){
            Toast.makeText(this, "Ban Teacher Please", Toast.LENGTH_SHORT).show();
        }else if(i==-1){
            Toast.makeText(this, "Hire someone First", Toast.LENGTH_SHORT).show();
        }else{
            obj.put("hired",requested.get(i));

            //Payment date
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            Date paymentDate = cal.getTime();
            obj.put("paymentDate",paymentDate);

            //removing from Requested
            requested.remove(i);
            obj.remove("requested");
            obj.addAll("requested",requested);

            // Removing hired teachers time
            interviewTime.set(i,"");
            interviewTime.removeAll(Collections.singletonList(""));
            obj.remove("interviewTime");
            obj.addAll("interviewTime",interviewTime);

            obj.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(PaymentOverdue.this, "DOne", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PaymentOverdue.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            int pos = getIntent().getIntExtra("pos",-1);
            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
            finish();
        }


    }

    public void rePost(View view) {
        if(ban.isEnabled()){
            Toast.makeText(this, "Ban Teacher Please", Toast.LENGTH_SHORT).show();
        }else{
            //lock to false
            obj.put("lock",false);
            //Null out values
            obj.remove("interviewDate");
            obj.remove("applied");
            obj.remove("requested");
            obj.remove("gTimeDate");
            obj.remove("interviewTime");

            obj.remove("hired");
            obj.remove("paymentDate");
            obj.remove("dueAmount");

            //Set Re-post to true / 1
            List<Integer> rp = obj.getList("reposted");
            rp.set(2,1);
            obj.remove("reposted");
            obj.addAll("reposted",rp);

            obj.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(PaymentOverdue.this, "Done", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PaymentOverdue.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            int pos = getIntent().getIntExtra("pos",-1);
            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
            finish();
        }

    }

    public void delete(View view) {
        if(ban.isEnabled()){
            Toast.makeText(this, "Ban Teacher Please", Toast.LENGTH_SHORT).show();
        }else{
            //Set delete to true / 1
            List<Integer> del = obj.getList("deleted");
            del.set(1,1);
            obj.remove("deleted");
            obj.addAll("deleted",del);
            obj.saveEventually();

            int pos = getIntent().getIntExtra("pos",-1);
            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
            finish();
        }
    }
}
