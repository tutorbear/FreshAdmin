package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;

public class PendingJob extends AppCompatActivity {
    ParseObject obj;
    TextView id,ngeo,name,salary,location,stdNumber,sClass,sub,curr,address,postId,tuitionType;
    EditText addressE,salaryE,email;
    Button call,delete,verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pending_job);
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
        postId = findViewById(R.id.postId);
        tuitionType = findViewById(R.id.tuitionType);

    }
    @SuppressLint("SetTextI18n")
    private void set() {

        id.setText("ID: "+ obj.getObjectId());
        ngeo.setText("Nego: "+ obj.getBoolean("negotiable"));

        name.setText(""+obj.getParseObject("createdBy").getString("guardianName"));
        salary.setText("Salary: "+ obj.get("salary").toString());
        location.setText("Location: "+ obj.getString("location"));
        stdNumber.setText("Number: "+ obj.get("numberOfStudents").toString());
        sClass.setText("Class: "+ obj.getString("class1")+","+ obj.getString("class2"));
        sub.setText("Subject1: "+ obj.getString("subject1")+"\nSubject2: "+ obj.getString("subject2"));
        address.setText("Address: "+ obj.getString("address"));
        curr.setText("curr: "+obj.get("curriculum"));
        postId.setText("postId:  "+obj.get("postId"));
        tuitionType.setText("Type: "+obj.get("tuitionType"));

    }

    public void verifyJob(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,Object> params = new HashMap<>();
                params.put("id",obj.getObjectId());
                params.put("username",obj.getParseObject("createdBy").get("username"));
                ParseCloud.callFunctionInBackground("verifyJob", params, new FunctionCallback<Boolean>() {
                    @Override
                    public void done(Boolean bool, ParseException e) {
                        if(e==null){
                            int pos = getIntent().getIntExtra("pos",-1);
                            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                            finish();
                        }else{
                            Toast.makeText(PendingJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("createdBy").get("username")));
        startActivity(intent);
    }

    public void delete(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are You Sure ?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("id",obj.getObjectId());
                    params.put("username",obj.getParseObject("createdBy").get("username"));
                    ParseCloud.callFunctionInBackground("deletePendingJobs", params, new FunctionCallback<Boolean>() {
                        @Override
                        public void done(Boolean bool, ParseException e) {
                            if(e==null){
                                int pos = getIntent().getIntExtra("pos",-1);
                                setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                                finish();
                            }else{
                                Toast.makeText(PendingJob.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
    }

}
