package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.freshadmin.databinding.ActivityFindUserBinding;
import com.example.freshadmin.databinding.ActivityStudentToTeacherBinding;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;

import okhttp3.internal.Util;

public class StudentToTeacher extends AppCompatActivity {
    private ActivityStudentToTeacherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentToTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void changeToTeacher(View view) {
        if(TextUtils.isEmpty(binding.number.getText()) || binding.number.getText().length()!=11){
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Change "+binding.number.getText()+" to Teacher?")
                    .setPositiveButton("YES", (dialog, id) -> {
                        // FIRE ZE MISSILES!
                        HashMap<String,String> params = new HashMap<>();
                        params.put("number",binding.number.getText().toString().trim());
                        ParseCloud.callFunctionInBackground("studentToTeacher", params, (FunctionCallback<ParseObject>) (object, e) -> {
                            if(e==null){
                                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // User cancelled the dialog
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        }

    }
}