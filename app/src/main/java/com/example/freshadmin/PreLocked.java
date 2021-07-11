package com.example.freshadmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.freshadmin.databinding.DeleteDialogBinding;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;

public class PreLocked extends AppCompatActivity {

    PreLockedAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> objs;
    EditText number;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_locked);
        init();
    }

    private void init() {
        bar = findViewById(R.id.pb);
        number = findViewById(R.id.number);
        recycle = findViewById(R.id.recycle);
        manager = new LinearLayoutManager(this);
    }

    public void fetchData(View view) {

        if (number.getText().toString().equals("") || number.getText().toString().length() < 11) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            bar.setVisibility(View.VISIBLE);
            HashMap<String, String> params = new HashMap<>();
            params.put("number", number.getText().toString());
            ParseCloud.callFunctionInBackground("getUnlockedJobs", params, new FunctionCallback<List<ParseObject>>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        if (!objects.isEmpty()) {
                            Toast.makeText(PreLocked.this, "Here", Toast.LENGTH_SHORT).show();
                            objs = objects;
                            customAdapter = new PreLockedAdapter(PreLocked.this, objs);
                            recycle.setAdapter(customAdapter);
                            recycle.setLayoutManager(manager);
                        } else {
                            Toast.makeText(PreLocked.this, "Nothing found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PreLocked.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    bar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void repost(final int pos) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Repost? Are you sure?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                bar.setVisibility(View.VISIBLE);
                HashMap<String, String> params = new HashMap<>();
                params.put("id", objs.get(pos).getObjectId());
                ParseCloud.callFunctionInBackground("repostPreLocked", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object objects, ParseException e) {
                        if (e == null) {
                            objs.remove(pos);
                            customAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PreLocked.this, "Something Wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        bar.setVisibility(View.GONE);
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

    public void delete(final int pos) {
        DeleteDialogBinding bindingDialog;

        Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_DayNight_Dialog);
        bindingDialog = DeleteDialogBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        bindingDialog.btnYes.setOnClickListener(v -> {
            if (!bindingDialog.editText.getText().toString().equals("")){
                bar.setVisibility(View.VISIBLE);
                HashMap<String, String> params = new HashMap<>();
                params.put("id", objs.get(pos).getObjectId());
                params.put("deleteReason", bindingDialog.editText.getText().toString());
                ParseCloud.callFunctionInBackground("deletePreLocked", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object objects, ParseException e) {
                        if (e == null) {
                            objs.remove(pos);
                            customAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(PreLocked.this, "Something Wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        bar.setVisibility(View.GONE);
                    }
                });
            } else {
                Toast.makeText(this, "please enter reason", Toast.LENGTH_SHORT).show();
            }
        });

//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Delete ? Are you sure?");
//        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                bar.setVisibility(View.VISIBLE);
//                HashMap<String, String> params = new HashMap<>();
//                params.put("id", objs.get(pos).getObjectId());
//                ParseCloud.callFunctionInBackground("deletePreLocked", params, new FunctionCallback<Object>() {
//                    @Override
//                    public void done(Object objects, ParseException e) {
//                        if (e == null) {
//                            objs.remove(pos);
//                            customAdapter.notifyDataSetChanged();
//                        } else {
//                            Toast.makeText(PreLocked.this, "Something Wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        bar.setVisibility(View.GONE);
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
//        alertDialogBuilder.show();
    }
}