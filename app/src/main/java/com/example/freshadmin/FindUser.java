package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.freshadmin.databinding.ActivityFindUserBinding;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.HashMap;

public class FindUser extends AppCompatActivity {
    private ActivityFindUserBinding binding;
    ParseObject profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    public void fetchData(View view) {
        binding.lock.setVisibility(View.GONE);
        binding.ban.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(binding.number.getText()) && binding.number.getText().length() == 11) {
            binding.pb.setVisibility(View.VISIBLE);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TeacherProfile");
            query.whereEqualTo("username", binding.number.getText().toString().trim());
            query.getFirstInBackground((object, e) -> {
                if (e == null) {
                    binding.pb.setVisibility(View.GONE);
                    profile = object;
                    Toast.makeText(this, "It's Teacher", Toast.LENGTH_SHORT).show();
                    binding.name.setText(profile.getString("fullName"));
                    binding.email.setText(profile.getString("email"));
                } else {

                    // something went wrong
                    if (e.getCode() == 101) {
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("StudentProfile");
                        query2.whereEqualTo("username", binding.number.getText().toString().trim());
                        query2.getFirstInBackground((object2, e2) -> {
                            if (e2 == null) {
                                binding.pb.setVisibility(View.GONE);
                                profile = object2;
                                Toast.makeText(this, "It's Student", Toast.LENGTH_SHORT).show();
                                binding.name.setText(profile.getString("guardianName"));
                                binding.email.setText(profile.getString("email"));
                                binding.lock.setVisibility(View.VISIBLE);
                                binding.ban.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(this, e2.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            binding.pb.setVisibility(View.GONE);
                        });
                    } else {
                        binding.pb.setVisibility(View.GONE);
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
        }
    }

    public void lock(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Lock User? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if(profile!=null){
                binding.pb.setVisibility(View.VISIBLE);
                profile.put("locked",true);
                profile.saveInBackground(e -> {
                    if (e==null){
                        Toast.makeText(FindUser.this, "Profile Lock Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FindUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    binding.pb.setVisibility(View.GONE);
                });
            }else{
                Toast.makeText(this, "Error,Try again", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No",null);

        alertDialogBuilder.show();
    }

    public void ban(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Ban User? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            binding.pb.setVisibility(View.VISIBLE);
            if(profile!=null){
                profile.put("banLock",true);
                profile.saveInBackground(e -> {
                    if (e==null){
                        Toast.makeText(FindUser.this, "Profile Ban Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FindUser.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    binding.pb.setVisibility(View.GONE);

                });
            }else{
                Toast.makeText(this, "Error,Try again", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("No",null);

        alertDialogBuilder.show();


    }
}