package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InterviewDay extends AppCompatActivity {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address,t1N,t2N,t3N,t1Time,t2Time,t3Time;
    HashMap<String,String> map;
    LinearLayout l1,l2,l3;
    Button h1,h2,h3;
    List<String> interviewTime;
    List<ParseObject> requested;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_day);
        init();
        set();
    }



    private void init() {
        obj= getIntent().getParcelableExtra("object");

        //Text views
        id = findViewById(R.id.jobIdInter);
        name = findViewById(R.id.nameInter);
        salary = findViewById(R.id.salaryInter);
        location = findViewById(R.id.locationInter);
        stdNumber = findViewById(R.id.studentNumberInter);
        sClass = findViewById(R.id.classInter);
        sub = findViewById(R.id.subjectInter);
        curr = findViewById(R.id.curriculumInter);
        address = findViewById(R.id.addressInter);
        //Teacher
        t1N = findViewById(R.id.t1NInter);
        t2N = findViewById(R.id.t2NInter);
        t3N = findViewById(R.id.t3NInter);
        t1Time= findViewById(R.id.t1TimeInter);
        t2Time = findViewById(R.id.t2TimeInter);
        t3Time = findViewById(R.id.t3TimeInter);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);
        l3 = findViewById(R.id.linearT3);

        //Map
        map = new HashMap<>();

        //Button
        h1 = findViewById(R.id.t1Hire);
        h2 = findViewById(R.id.t2Hire);
        h3 = findViewById(R.id.t3Hire);
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

            interviewTime = obj.getList("interviewTime");
            if(interviewTime!=null && !interviewTime.isEmpty()){
                for (int i = 0; i < interviewTime.size(); i++) {
                    if(i==0){

                            t1Time.setText(interviewTime.get(i));
                            t1Time.setVisibility(View.VISIBLE);
                    }else if(i==1){
                            t2Time.setText(interviewTime.get(i));
                            t2Time.setVisibility(View.VISIBLE);
                    }else{
                            t3Time.setText(interviewTime.get(i));
                            t3Time.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
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

    public void fine(View view) {
        //Getting id and Setting corresponding time to ""
        String id;
        if (view.getId()==R.id.t1Fine){
            l1.setVisibility(View.GONE);
            t1Time.setVisibility(View.GONE);
            obj.put("timeDeleted",interviewTime.get(0));
            interviewTime.set(0,"");

            id = map.get("l1");
        }else if(view.getId()==R.id.t2Fine){
            l2.setVisibility(View.GONE);
            t2Time.setVisibility(View.GONE);
            obj.put("timeDeleted",interviewTime.get(1));
            interviewTime.set(1,"");

            id = map.get("l2");
        }else{
            l3.setVisibility(View.GONE);
            t3Time.setVisibility(View.GONE);
            obj.put("timeDeleted",interviewTime.get(2));
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

        //Adding to fine
        obj.add("fined",requested.get(removeIndex));

        //removing from Requested
        requested.remove(removeIndex);
        obj.remove("requested");
        obj.addAll("requested",requested);

        //Modifying interviewTime array
        obj.remove("interviewTime");
        obj.addAll("interviewTime",interviewTime);

        obj.saveEventually();

    }

    public void hire(View view) {
        if (view.getId()==R.id.t1Hire){
            h1.setTextColor(Color.parseColor("#D81B60"));

            h2.setTextColor(Color.parseColor("#050505"));
            h3.setTextColor(Color.parseColor("#050505"));
        }else if(view.getId()==R.id.t2Hire){
            h2.setTextColor(Color.parseColor("#D81B60"));

            h1.setTextColor(Color.parseColor("#050505"));
            h3.setTextColor(Color.parseColor("#050505"));
        }else{
            h3.setTextColor(Color.parseColor("#D81B60"));

            h2.setTextColor(Color.parseColor("#050505"));
            h1.setTextColor(Color.parseColor("#050505"));
        }
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").getString("phone")));
        startActivity(intent);
    }

    public void submit(View view) {
        int colorH1 = h1.getTextColors().getDefaultColor();
        int colorH2 = h2.getTextColors().getDefaultColor();
        int colorH3 = h3.getTextColors().getDefaultColor();

        String id=null;
        if(colorH1==Color.parseColor("#D81B60")){
            id = map.get("l1");

        }else if(colorH2==Color.parseColor("#D81B60")){
            id = map.get("l2");

        }else if(colorH3==Color.parseColor("#D81B60")){
            id = map.get("l3");
        }

        if(id==null){
            Toast.makeText(this, "Hire someone First", Toast.LENGTH_SHORT).show();
        }else{

            int index = -1;

            for (int i = 0; i < requested.size(); i++) {
                if(id.equals(requested.get(i).getObjectId())){
                    index = i;
                }
            }
            obj.put("hired",requested.get(index));

            //Payment date
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.HOUR_OF_DAY, 8);
            Date paymentDate = cal.getTime();
            obj.put("paymentDate",paymentDate);

            //removing from Requested
            requested.remove(index);
            obj.remove("requested");
            obj.addAll("requested",requested);

            // Removing hired teachers time
            interviewTime.set(index,"");
            interviewTime.removeAll(Collections.singletonList(""));
            obj.remove("interviewTime");
            obj.addAll("interviewTime",interviewTime);


            obj.saveEventually();

            int pos = getIntent().getIntExtra("pos",-1);
            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
            finish();
        }
    }

    public void rePost(View view) {
        //lock to false
        obj.put("lock",false);
        //Null out values
        obj.remove("interviewDate");
        obj.remove("applied");
        obj.remove("requested");
        obj.remove("gTimeDate");
        obj.remove("interviewTime");

        //Set Re-post to true / 1
        List<Integer> rp = obj.getList("reposted");
        rp.set(1,1);
        obj.remove("reposted");
        obj.addAll("reposted",rp);

        obj.saveEventually();
        int pos = getIntent().getIntExtra("pos",-1);
        setResult(RESULT_OK,new Intent().putExtra("pos",pos));
        finish();
    }

    public void delete(View view) {
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
