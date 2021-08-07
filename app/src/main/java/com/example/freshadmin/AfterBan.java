package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshadmin.databinding.DeleteDialogBinding;
import com.example.freshadmin.databinding.DetailsDialogBinding;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class AfterBan extends AppCompatActivity {
    ParseObject obj;
    TextView id, name, salary, location, stdNumber, sClass, sub, curr, address, t1N, t2N, t1Time, t2Time;
    LinearLayout l1, l2;
    Button h1, h2, d1, d2;
    List<String> interviewTime;
    List<ParseObject> requested;

    Bitmap bmp1, bmp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_ban);
        init();
        set();
    }

    private void init() {
        obj = getIntent().getParcelableExtra("object");

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
        t1Time = findViewById(R.id.t1TimeInter);
        t2Time = findViewById(R.id.t2TimeInter);

        //linear layouts
        l1 = findViewById(R.id.linearT1);
        l2 = findViewById(R.id.linearT2);

        //Button
        h1 = findViewById(R.id.t1Hire);
        h2 = findViewById(R.id.t2Hire);

        d1 = findViewById(R.id.t1Details);
        d2 = findViewById(R.id.t2Details);
    }

    private void set() {
        id.setText("ID: " + obj.getObjectId());
        name.setText("" + obj.getParseObject("createdBy").getString("guardianName"));
        salary.setText("Salary: " + obj.get("salary").toString());
        location.setText("Location: " + obj.getString("location"));
        stdNumber.setText("Number: " + obj.get("numberOfStudents").toString());
        sClass.setText("Class: " + obj.getString("class1") + "," + obj.getString("class2"));
        sub.setText("Subject1: " + obj.getString("subject1") + "\nSubject2: " + obj.getString("subject2"));
        address.setText("Address: " + obj.getString("address"));

        // Setting visibility to false
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);

        t1Time.setVisibility(View.GONE);
        t2Time.setVisibility(View.GONE);
        //-------------------------------------

        requested = obj.getList("requested");


        if (requested != null && !requested.isEmpty()) {
            for (int i = 0; i < requested.size(); i++) {
                if (i == 0) {
                    l1.setVisibility(View.VISIBLE);
                    t1N.setText(requested.get(0).getString("fullName"));
                    if (requested.get(0).getBoolean("banLock"))
                        t1N.setTextColor(Color.RED);

                    requested.get(0).getParseFile("proPic").getDataInBackground((data, e) -> {
                        bmp1 = BitmapFactory.decodeByteArray(data, 0, data.length);
                    });
                } else if (i == 1) {
                    l2.setVisibility(View.VISIBLE);
                    t2N.setText(requested.get(1).getString("fullName"));
                    if (requested.get(1).getBoolean("banLock"))
                        t2N.setTextColor(Color.RED);

                    requested.get(1).getParseFile("proPic").getDataInBackground((data, e) -> {
                        bmp2 = BitmapFactory.decodeByteArray(data, 0, data.length);
                    });
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
                    }
                }
            }
        }
    }

    private JSONObject parseObjectToJson(ParseObject parseObject) throws ParseException, JSONException {
        JSONObject jsonObject = new JSONObject();
        parseObject.fetchIfNeeded();
        Set<String> keys = parseObject.keySet();
        for (String key : keys) {
            Object objectValue = parseObject.get(key);
            if (objectValue instanceof ParseObject) {
                jsonObject.put(key, parseObjectToJson(parseObject.getParseObject(key)));
                // keep in mind about "pointer" to it self, will gain stackoverlow
            } else if (objectValue instanceof ParseRelation) {
                // handle relation
            } else {
                jsonObject.put(key, objectValue.toString());
            }
        }
        return jsonObject;
    }

    public void hire(View view) {
        if (view.getId() == R.id.t1Hire) {
            h1.setTextColor(Color.parseColor("#D81B60"));
            h2.setTextColor(Color.parseColor("#050505"));
        } else if (view.getId() == R.id.t2Hire) {
            h2.setTextColor(Color.parseColor("#D81B60"));
            h1.setTextColor(Color.parseColor("#050505"));
        }
    }

    public void submit(View view) {
        int colorH1 = h1.getTextColors().getDefaultColor();
        int colorH2 = h2.getTextColors().getDefaultColor();

        int idx = -1;
        if (colorH1 == Color.parseColor("#D81B60")) {
            idx = 0;
        } else if (colorH2 == Color.parseColor("#D81B60")) {
            idx = 2;
        }

        final int index = idx;
        if (index == -1) {
            Toast.makeText(this, "Hire someone First", Toast.LENGTH_SHORT).show();
        } else {

            Log.d("indexbbb", id + "");
            //Payment date
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            cal.add(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 2);
            final Date paymentDate = cal.getTime();

            // Removing hired teachers time
            interviewTime.set(index, "");

            // copying the ArrayList zoo to the ArrayList list
            final List<String> tempArr = new ArrayList<>(interviewTime);
            tempArr.removeAll(Collections.singletonList(""));


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("submit, Are you sure?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("id", obj.getObjectId());
                    params.put("tId", requested.get(index).getObjectId());
                    params.put("interviewTime", tempArr);
                    params.put("paymentDate", paymentDate);
                    ParseCloud.callFunctionInBackground("hireTeacher", params, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object object, ParseException e) {
                            if (e == null) {
                                Toast.makeText(AfterBan.this, "Done Hire", Toast.LENGTH_SHORT).show();
                                int pos = getIntent().getIntExtra("pos", -1);
                                setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                                finish();
                            } else {
                                Toast.makeText(AfterBan.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        List<ParseObject> requested = obj.getList("requested");

        String num = null;
        if (view.getId() == R.id.t1Call) {
            num = requested.get(0).getString("username");
        } else if (view.getId() == R.id.t2Call) {
            num = requested.get(1).getString("username");

        }

        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);
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
                            Toast.makeText(AfterBan.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + obj.getParseObject("createdBy").get("username")));
        startActivity(intent);
    }

    public void delete(View view) {
        DeleteDialogBinding bindingDialog;

        Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        bindingDialog = DeleteDialogBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        bindingDialog.btnYes.setOnClickListener(v -> {
            if (!bindingDialog.editText.getText().toString().equals("")) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", obj.getObjectId());
                params.put("num", 2);
                params.put("deleteReason", bindingDialog.editText.getText().toString());
                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if (e == null) {
                            int pos = getIntent().getIntExtra("pos", -1);
                            setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                            finish();
                        } else {
                            Toast.makeText(AfterBan.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "please enter reason", Toast.LENGTH_SHORT).show();
            }
        });

//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Delete, Are you sure?");
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                HashMap<String,Object> params = new HashMap<>();
//                params.put("id",obj.getObjectId());
//                params.put("num",2);
//                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
//                    @Override
//                    public void done(Object object, ParseException e) {
//                        if(e==null){
//                            int pos = getIntent().getIntExtra("pos",-1);
//                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
//                            finish();
//                        }else{
//                            Toast.makeText(AfterBan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        alertDialogBuilder.show();


    }

    public void detailsT(View view) {
        ParseObject teacherObj;

        DetailsDialogBinding bindingDialog;

        Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        bindingDialog = DetailsDialogBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (d1 == view) {
            teacherObj = requested.get(0);
            bindingDialog.image.setImageBitmap(bmp1);
        } else {
            teacherObj = requested.get(1);
            bindingDialog.image.setImageBitmap(bmp2);
        }
        setDetailsDialog(bindingDialog, teacherObj);

        dialog.show();

    }

    private void setDetailsDialog(DetailsDialogBinding bindingDialog, ParseObject teacherObj) {
        bindingDialog.fullName.setText(teacherObj.getString("fullName"));
        bindingDialog.email.setText(teacherObj.getString("email"));
        bindingDialog.school.setText(teacherObj.getString("school"));
        bindingDialog.college.setText(teacherObj.getString("college"));
        bindingDialog.university.setText(teacherObj.getString("university"));
        bindingDialog.currentEducation.setText(teacherObj.getString("currentEducation"));
        bindingDialog.curriculum.setText(teacherObj.getString("curriculum"));
        bindingDialog.background.setText(teacherObj.getString("background"));
        bindingDialog.city.setText(teacherObj.getString("city"));
        bindingDialog.location.setText(teacherObj.getString("location"));
        bindingDialog.username.setText(teacherObj.getString("username"));
    }

}