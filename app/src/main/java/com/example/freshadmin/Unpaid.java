package com.example.freshadmin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

public class Unpaid extends AppCompatActivity {
    ParseObject obj;
    TextView jobId,location,subjects,dueAmount,dueDate,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
        init();
        set();
    }

    private void init() {
        //Text Views
        jobId = findViewById(R.id.jobIdUP);
        location = findViewById(R.id.locationUP);
        subjects = findViewById(R.id.subjectUP);
        dueAmount = findViewById(R.id.dueAmountUP);
        dueDate = findViewById(R.id.dueDateUP);
        name = findViewById(R.id.nameUP);

    }

    private void set() {
        obj= getIntent().getParcelableExtra("object");

        jobId.setText("ID: "+obj.getObjectId());
        location.setText("Location: "+ obj.getString("location"));

        List x = obj.getList("sub");

        subjects.setText("Subject1: "+ x.get(0)+"\nSubject2: "+ ((x.size() == 2) ? x.get(1) : ""));
        dueAmount.setText("Due Amount: "+obj.getInt("dueAmount"));

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(obj.getDate("date"));
        dueDate.setText("Due Date: "+date);

        name.setText(""+obj.get("name"));
    }


    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.get("phone")));
        startActivity(intent);
    }

    public void sendN(View view) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("username", obj.getString("username"));
        hashMap.put("id", obj.getObjectId());
        ParseCloud.callFunctionInBackground("warningNoti", hashMap, new FunctionCallback<HashMap>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void done(HashMap s, ParseException e) {
                if (e == null) {
                    Toast.makeText(Unpaid.this, s.get("result")+"", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Unpaid.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void check(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Unpaid");
        query.getInBackground(obj.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e==null){
                    Toast.makeText(Unpaid.this, "Not paid", Toast.LENGTH_SHORT).show();
                }else{
                    if(e.getCode()==101){
                        Toast.makeText(Unpaid.this, "Paid", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Unpaid.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}