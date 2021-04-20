package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

public class Bkash extends AppCompatActivity {
    EditText trx, amt, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash);
        init();
    }

    private void init() {
        trx = findViewById(R.id.trxId);
        amt = findViewById(R.id.amount);
        date = findViewById(R.id.date);
    }


    public void submit(View view) {
        if (TextUtils.isEmpty(trx.getText()) || TextUtils.isEmpty(amt.getText()) || TextUtils.isEmpty(date.getText())) {
            Toast.makeText(this, "Field missing", Toast.LENGTH_SHORT).show();
        } else if(trx.getText().toString().trim().length()!=10){
            Toast.makeText(this, "Invalid TRX", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> params = new HashMap<>();
            params.put("trxId",trx.getText().toString().trim());
            params.put("amount",Integer.parseInt(amt.getText().toString().trim()));
            params.put("paidDate",date.getText().toString().trim());
            ParseCloud.callFunctionInBackground("inputBkash", params, new FunctionCallback<Object>() {
                @Override
                public void done(Object object, ParseException e) {
                    if (e==null){
                        Toast.makeText(Bkash.this, "Done", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Bkash.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}