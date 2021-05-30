package com.example.freshadmin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.function.ToIntBiFunction;

public class InterviewDay extends AppCompatActivity {
    ParseObject obj;
    TextView fromApp,id,type, nego, name, salary, location, stdNumber, sClass, sub, curr, address, t1N, t2N, t3N, t1Time, t2Time, t3Time;
    HashMap<String, String> map;
    EditText dateAndTime;
    LinearLayout l1, l2, l3;
    Button h1, h2, h3, t1view, t2view, t3view;
    List<String> interviewTime;
    List<ParseObject> requested;
    List<String> picArray;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_day);
        init();
        getPictures();
        set();
    }

    private void getPictures() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", obj.getObjectId());
        ParseCloud.callFunctionInBackground("getPicInterview", params, new FunctionCallback<ArrayList<String>>() {
            @Override
            public void done(ArrayList<String> base64Array, ParseException e) {
                if (e == null) {
                    picArray = base64Array;
                } else {
                    Toast.makeText(InterviewDay.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }
        });
    }


    private void init() {
        obj = getIntent().getParcelableExtra("object");
        //progressbar
        pb = findViewById(R.id.pb_inter);
        //Text views
        fromApp = findViewById(R.id.fromApp);
        id = findViewById(R.id.id);
        type = findViewById(R.id.tuitionType);
        nego = findViewById(R.id.nego);
        name = findViewById(R.id.name);
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
        t1Time = findViewById(R.id.t1TimeInter);
        t2Time = findViewById(R.id.t2TimeInter);
        t3Time = findViewById(R.id.t3TimeInter);

        //Edit Text
        dateAndTime = findViewById(R.id.dateAndTime);

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

        t1view = findViewById(R.id.tview1);
        t2view = findViewById(R.id.tview2);
        t3view = findViewById(R.id.tview3);

        //Edit

    }

    @SuppressLint("SetTextI18n")
    private void set() {
        id.setText("ID: " + obj.getObjectId());
        type.setText(obj.getString("tuitionType"));
        nego.setText("Nego: "+ obj.getBoolean("negotiable"));
        name.setText("" + obj.getParseObject("createdBy").getString("guardianName"));
        salary.setText("Salary: " + obj.get("salary").toString());
        location.setText("Location: " + obj.getString("location"));
        stdNumber.setText("Number: " + obj.get("numberOfStudents").toString());
        sClass.setText("Class: " + obj.getString("class1") + "," + obj.getString("class2"));
        sub.setText("Subject1: " + obj.getString("subject1") + "\nSubject2: " + obj.getString("subject2"));
        address.setText("Address: " + obj.getString("address"));

        //
        dateAndTime.setText(obj.getString("gTimeDate"));
        fromApp.setText("From App: "+obj.getBoolean("fromApp"));

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
        if (requested != null && !requested.isEmpty()) {
            for (int i = 0; i < requested.size(); i++) {
                if (i == 0) {
                    t1view.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    t1N.setText(requested.get(0).getString("fullName"));
                    if (requested.get(0).getBoolean("banLock"))
                        t1N.setTextColor(Color.RED);
                    map.put("l1", requested.get(i).getObjectId());
                } else if (i == 1) {
                    t2view.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(1).getString("fullName"));

                    if (requested.get(1).getBoolean("banLock"))
                        t2N.setTextColor(Color.RED);

                    map.put("l2", requested.get(i).getObjectId());
                } else {
                    t3view.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    t3N.setText(requested.get(2).getString("fullName"));

                    if (requested.get(2).getBoolean("banLock"))
                        t3N.setTextColor(Color.RED);

                    map.put("l3", requested.get(i).getObjectId());
                }
            }

            interviewTime = obj.getList("interviewTime");
            if (interviewTime != null && !interviewTime.isEmpty()) {
                for (int i = 0; i < interviewTime.size(); i++) {
                    if (i == 0) {
                        t1Time.setText(interviewTime.get(i));
                        t1Time.setVisibility(View.VISIBLE);
                    } else if (i == 1) {
                        t2Time.setText(interviewTime.get(i));
                        t2Time.setVisibility(View.VISIBLE);
                    } else {
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
        if (view.getId() == R.id.t1Call) {
            id = map.get("l1");
        } else if (view.getId() == R.id.t2Call) {
            id = map.get("l2");
        } else {
            id = map.get("l3");
        }

        ParseObject temp = null;
        for (int i = 0; i < requested.size(); i++) {
            if (id.equals(requested.get(i).getObjectId())) {
                temp = requested.get(i);
            }
        }

        intent.setData(Uri.parse("tel:" + temp.getString("username")));
        startActivity(intent);
    }

    public void fine(final View view) {
        //Getting id and Setting corresponding time to ""

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Fine teacher? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id;
                if (view.getId() == R.id.t1Fine) {
                    interviewTime.set(0, "");
                    id = map.get("l1");
                    callCloudFine(id, l1, t1Time, t1view);
                } else if (view.getId() == R.id.t2Fine) {
                    interviewTime.set(1, "");
                    id = map.get("l2");
                    callCloudFine(id, l2, t2Time, t2view);
                } else {
                    interviewTime.set(2, "");
                    id = map.get("l3");
                    callCloudFine(id, l3, t3Time, t3view);
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

    public void callCloudFine(String id, final LinearLayout lin, final TextView time, final Button btn) {
        final List<String> tempInterviewTime = new ArrayList<>(interviewTime);
        tempInterviewTime.removeAll(Collections.singletonList(""));
        HashMap<String, Object> params = new HashMap<>();
        params.put("tId", id);
        params.put("id", obj.getObjectId());
        params.put("interviewTime", tempInterviewTime);

        ParseCloud.callFunctionInBackground("fineTeacher", params, new FunctionCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    obj = object;
                    Toast.makeText(InterviewDay.this, "Done", Toast.LENGTH_SHORT).show();
                    lin.setVisibility(View.GONE);
                    time.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(InterviewDay.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hire(View view) {
        if (view.getId() == R.id.t1Hire) {
            h1.setTextColor(Color.parseColor("#D81B60"));
            h2.setTextColor(Color.parseColor("#050505"));
            h3.setTextColor(Color.parseColor("#050505"));
        } else if (view.getId() == R.id.t2Hire) {
            h2.setTextColor(Color.parseColor("#D81B60"));
            h1.setTextColor(Color.parseColor("#050505"));
            h3.setTextColor(Color.parseColor("#050505"));
        } else {
            h3.setTextColor(Color.parseColor("#D81B60"));
            h2.setTextColor(Color.parseColor("#050505"));
            h1.setTextColor(Color.parseColor("#050505"));
        }
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + obj.getParseObject("createdBy").get("username")));
        startActivity(intent);
    }

    public void submit(View view) {
        int colorH1 = h1.getTextColors().getDefaultColor();
        int colorH2 = h2.getTextColors().getDefaultColor();
        int colorH3 = h3.getTextColors().getDefaultColor();

        String id = null;
        if (colorH1 == Color.parseColor("#D81B60")) {
            id = map.get("l1");

        } else if (colorH2 == Color.parseColor("#D81B60")) {
            id = map.get("l2");

        } else if (colorH3 == Color.parseColor("#D81B60")) {
            id = map.get("l3");
        }

        if (id == null) {
            Toast.makeText(this, "Hire someone First", Toast.LENGTH_SHORT).show();
        } else {
            String finalId = id;

            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("Hire Today?");
            ab.setPositiveButton("Today", (dialogInterface, j) -> {
                submit2(finalId,true);
            });
            ab.setNegativeButton("Tomorrow", (dialogInterface, i) -> {
                submit2(finalId,false);
            });
            ab.show();
        }
    }

    public void submit2(String id,Boolean isToday){
        final String finalId = id;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Hire Teacher? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, j) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                int index = -1;

                for (int i = 0; i < requested.size(); i++) {
                    if (finalId.equals(requested.get(i).getObjectId())) {
                        index = i;
                    }
                }


                //Payment date
                Date paymentDate;
                if (!isToday) {
                    //Add one day
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    cal.set(Calendar.HOUR_OF_DAY, 2);
                    paymentDate = cal.getTime();
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                    cal.set(Calendar.HOUR_OF_DAY, 2);
                    paymentDate = cal.getTime();
                }

                // Removing hired teachers time
                interviewTime.set(index, "");

                // copying the ArrayList zoo to the ArrayList list
                List<String> tempArr = new ArrayList<>(interviewTime);
                tempArr.removeAll(Collections.singletonList(""));

                HashMap<String, Object> params = new HashMap<>();
                params.put("id", obj.getObjectId());
                params.put("tId", finalId);
                params.put("interviewTime", tempArr);
                params.put("paymentDate", paymentDate);
                params.put("istoday", isToday);

                ParseCloud.callFunctionInBackground("hireTeacher", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if (e == null) {
                            Toast.makeText(InterviewDay.this, "Done Hire", Toast.LENGTH_SHORT).show();
                            int pos = getIntent().getIntExtra("pos", -1);
                            setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                            finish();
                        } else {
                            Toast.makeText(InterviewDay.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(InterviewDay.this, "Need android 8 or higher", Toast.LENGTH_SHORT).show();
            }

        });

        alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        alertDialogBuilder.show();
    }
    public void rePost(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Repost, Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", obj.getObjectId());
                params.put("num", 2);
                ParseCloud.callFunctionInBackground("repost", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if (e == null) {
                            int pos = getIntent().getIntExtra("pos", -1);
                            setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                            finish();
                        } else {
                            Toast.makeText(InterviewDay.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                pb.setVisibility(View.VISIBLE);
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", obj.getObjectId());
                params.put("num", 2);
                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if (e == null) {
                            int pos = getIntent().getIntExtra("pos", -1);
                            setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                            finish();
                            pb.setVisibility(View.GONE);
                        } else {
                            pb.setVisibility(View.GONE);
                            Toast.makeText(InterviewDay.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("SetTextI18n")
    public void seeDetails(View view) {
        String id;

        List<ParseObject> requested = obj.getList("requested");

        if (view.getId() == R.id.tview1) {
            id = map.get("l1");
        } else if (view.getId() == R.id.tview2) {
            id = map.get("l2");
        } else {
            id = map.get("l3");
        }
        int index = -1;
        ParseObject tObj = null;
        for (int i = 0; i < requested.size(); i++) {
            if (id.equals(requested.get(i).getObjectId())) {
                tObj = requested.get(i);
                index = i;
            }
        }

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_inter, null);

        if (picArray != null) {
            ImageView v = customLayout.findViewById(R.id.imageView);
            byte[] proPic = Base64.decode(picArray.get(index), Base64.DEFAULT);
            Glide.with(this).load(proPic).into(v);
        }


        ((TextView) customLayout.findViewById(R.id.fullName)).setText("Name: " + tObj.getString("fullName"));
        ((TextView) customLayout.findViewById(R.id.school)).setText("School: " + tObj.getString("school"));
        ((TextView) customLayout.findViewById(R.id.uni)).setText("UNI: " + tObj.getString("university"));

        builder.setView(customLayout);
        builder.show();
    }

    public void saveChanges(View view) {
        if (dateAndTime.getText().length() != 0)
            obj.put("gTimeDate", dateAndTime.getText().toString());

        obj.saveInBackground(e -> {
            if(e==null){
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
