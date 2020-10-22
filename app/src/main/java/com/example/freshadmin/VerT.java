package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class VerT extends AppCompatActivity {
    ParseObject obj;
    TextView name,username,gender,currentEdu,curriculum,background,schoolName,uniName,uniProgram,address,phone;
    ImageView proPic,nId,certificate,idCard;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_t);
        init();
        set();
        fetchPic();
    }

    private void fetchPic() {
        progress.bringToFront();
        progress.setVisibility(View.VISIBLE);
        HashMap<String,Object> params = new HashMap<>();
        params.put("id",obj.getObjectId());
        ParseCloud.callFunctionInBackground("fetchPhotoT",params, new FunctionCallback<ArrayList<String>>() {
            @Override
            public void done(ArrayList base64Array, ParseException e) {
                if(e==null){
                    byte[] cer = Base64.decode(base64Array.get(0).toString(), Base64.DEFAULT);
                    byte[] nid = Base64.decode(base64Array.get(1).toString(), Base64.DEFAULT);
                    byte[] pic = Base64.decode(base64Array.get(2).toString(), Base64.DEFAULT);
                    Glide.with(VerT.this).load(cer).into(certificate);
                    Glide.with(VerT.this).load(nid).into(nId);
                    Glide.with(VerT.this).load(pic).into(proPic);
                    if(base64Array.size()==4){
                        byte[] uni = Base64.decode(base64Array.get(3).toString(), Base64.DEFAULT);
                        Glide.with(VerT.this).load(uni).into(idCard);
                    }

                }else{
                    Toast.makeText(VerT.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //TextViews
        name=findViewById(R.id.nameTP);
        username=findViewById(R.id.usernameTP);
        gender=findViewById(R.id.genderTP);
        currentEdu=findViewById(R.id.cEduTP);
        curriculum=findViewById(R.id.curriculumTP);
        background=findViewById(R.id.backgroundTP);
        schoolName=findViewById(R.id.schoolNameTP);
        uniName=findViewById(R.id.uniNameTP);
        uniProgram=findViewById(R.id.uniProgramTP);
        address=findViewById(R.id.addressTP);
        phone=findViewById(R.id.phoneTP);
        //Image Vies
        proPic = findViewById(R.id.proPicTP);
        nId = findViewById(R.id.nIdTP);
        certificate=findViewById(R.id.certificateTP);
        idCard=findViewById(R.id.idCardTP);
        //progress bar
        progress = findViewById(R.id.pb_verT);
    }

    private void set() {
        // Text Views
        name.setText("full Name: "+obj.getString("fullName"));
        username.setText("username: "+obj.getString("username"));
        gender.setText("gender: "+obj.getString("gender"));
        currentEdu.setText("currentEducation: "+obj.getString("currentEducation"));
        curriculum.setText("curriculum: "+obj.getString("curriculum"));
        background.setText("background: "+obj.getString("background"));
        schoolName.setText("schoolName: "+obj.getString("school"));
        address.setText("address: "+obj.getString("address"));
        phone.setText(obj.getString("phone"));
        //Images


        if (obj.getString("university")!=null){
            uniName.setText("university: "+obj.getString("university"));
            uniProgram.setText("uniProgram: "+obj.getString("uniProgram"));
        }else {
            uniName.setVisibility(View.GONE);
            uniProgram.setVisibility(View.GONE);
            idCard.setVisibility(View.GONE);
        }
    }

    public void verifyT(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obj.put("verified",true);
                obj.saveEventually();
                int pos = getIntent().getIntExtra("pos",-1);
                setResult(RESULT_OK,new Intent().putExtra("pos",pos));
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    public void callT(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.getString("phone")));
        startActivity(intent);
    }

    public void rejectT(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obj.put("verFailed", true);
                obj.put("comment","NID");
                obj.saveEventually();
                int pos = getIntent().getIntExtra("pos", -1);
                setResult(RESULT_OK, new Intent().putExtra("pos", pos));
                finish();
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
