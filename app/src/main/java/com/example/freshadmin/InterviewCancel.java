package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshadmin.databinding.DeleteDialogBinding;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;

public class InterviewCancel extends AppCompatActivity {
    ParseObject obj;
    TextView id,name,salary,location,stdNumber,sClass,sub,curr,address;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_cancel);
        init();
        set();
    }

    private void init() {
        obj= getIntent().getParcelableExtra("object");
        //progressbar
        pb=findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
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
    }
    @SuppressLint("SetTextI18n")
    private void set() {
        id.setText("ID: " + obj.getObjectId());
        name.setText("" + obj.getParseObject("createdBy").getString("guardianName"));
        salary.setText("Salary: " + obj.get("salary").toString());
        location.setText("Location: " + obj.getString("location"));
        stdNumber.setText("Number: " + obj.get("numberOfStudents").toString());
        sClass.setText("Class: " + obj.getString("class1") + "," + obj.getString("class2"));
        sub.setText("Subject1: " + obj.getString("subject1") + "\nSubject2: " + obj.getString("subject2"));
        address.setText("Address: " + obj.getString("address"));
    }

    public void cancelNDelete(View view) {

        DeleteDialogBinding bindingDialog;

        Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        bindingDialog = DeleteDialogBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        bindingDialog.btnYes.setOnClickListener(v -> {
            if (!bindingDialog.editText.getText().toString().equals("")){
                pb.setVisibility(View.VISIBLE);
                HashMap<String,Object> params = new HashMap<>();
                params.put("deleteReason", bindingDialog.editText.getText().toString());
                params.put("id",obj.getObjectId());
                params.put("num",3);
                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if(e==null){
                            int pos = getIntent().getIntExtra("pos",-1);
                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                            finish();
                            pb.setVisibility(View.GONE);
                        }else{
                            pb.setVisibility(View.GONE);
                            Toast.makeText(InterviewCancel.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "please enter reason", Toast.LENGTH_SHORT).show();
            }
        });


//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Cancel and Delete, Are you sure?");
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                pb.setVisibility(View.VISIBLE);
//                HashMap<String,Object> params = new HashMap<>();
//                params.put("id",obj.getObjectId());
//                params.put("num",3);
//                ParseCloud.callFunctionInBackground("delete", params, new FunctionCallback<Object>() {
//                    @Override
//                    public void done(Object object, ParseException e) {
//                        if(e==null){
//                            int pos = getIntent().getIntExtra("pos",-1);
//                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
//                            finish();
//                            pb.setVisibility(View.GONE);
//                        }else{
//                            pb.setVisibility(View.GONE);
//                            Toast.makeText(InterviewCancel.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
}