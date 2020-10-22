package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.text.DateFormat;
import java.util.List;

public class PaidJob extends AppCompatActivity {
    ParseObject obj;
    TextView jobId,location,subjects,dueAmount,paidAmount,dueDate,paidDate,trxId,matchStatus,name;
    Button doMatch,confirm,callT;
    boolean m = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_job);
        init();
        set();
    }

    private void init() {
        //Text Views
        jobId = findViewById(R.id.jobIdP);
        location = findViewById(R.id.locationP);
        subjects = findViewById(R.id.subjectP);
        dueAmount = findViewById(R.id.dueAmount);
        paidAmount = findViewById(R.id.paidAmount);
        dueDate = findViewById(R.id.dueDate);
        paidDate = findViewById(R.id.paidDate);
        trxId = findViewById(R.id.trxId);
        matchStatus = findViewById(R.id.matchStatus);
        name = findViewById(R.id.nameP);

        //Buttons
        doMatch = findViewById(R.id.matchId);
        confirm = findViewById(R.id.submitP);
        callT = findViewById(R.id.callTP);
    }

    private void set() {
        obj= getIntent().getParcelableExtra("object");

        jobId.setText("ID: "+obj.getObjectId());
        location.setText("Location: "+ obj.getString("location"));
        subjects.setText("Subject1: "+ obj.getString("subject1")+"\nSubject2: "+ obj.getString("subject2"));

        dueAmount.setText("Due Amount: "+obj.getInt("dueAmount"));

        String date = DateFormat.getDateInstance(DateFormat.FULL).format(obj.getDate("paymentDate"));
        dueDate.setText("Due Date: "+date);
        paidDate.setText("Paid Date: "+DateFormat.getDateInstance(DateFormat.FULL).format(obj.getDate("paidDate")));

        trxId.setText("trxID: "+obj.get("trxId"));
        name.setText(""+obj.getParseObject("hired").getString("fullName"));
    }

    public void match(View view) {
        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);
        String trx = obj.getString("trxId").trim();
        cursor.moveToFirst();
        while  (cursor.moveToNext()){
            String address = cursor.getString(1);
            String body = cursor.getString(3);
            if(address.equals("bKash")){
                String[] splitted = body.split("\\s+");
                //Traversing the splitted array to find trxid
                for (String x : splitted) {
                    if(x.equalsIgnoreCase(trx)){
                        matchStatus.setText("Match found");
                        matchStatus.setTextColor(Color.GREEN);
                        // Now searching for The amount
                        if(splitted[0].equals("You")){
                            paidAmount.setText("Amount Paid: "+splitted[4].substring(0, splitted[4].length() - 3));
                        }else if(splitted[0].equals("Cash")){
                            paidAmount.setText("Amount Paid: "+splitted[3].substring(0, splitted[4].length() - 3));
                        }
                        m = true;
                    }
                }
            }
        }
        if(!m){
            matchStatus.setText("Match not found");
            matchStatus.setTextColor(Color.RED);
        }
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getParseObject("hired").get("phone")));
        startActivity(intent);
    }

    public void confirm(View view) {
        if(!m){
            Toast.makeText(this, "Can't confirm , no match found", Toast.LENGTH_SHORT).show();
        }else {
            //Set delete to true / 1
            List<Integer> del = obj.getList("deleted");
            del.set(3,1);
            obj.put("deleted",del);
            obj.saveEventually();
            int pos = getIntent().getIntExtra("pos",-1);
            setResult(RESULT_OK,new Intent().putExtra("pos",pos));
            finish();
        }
    }
}
