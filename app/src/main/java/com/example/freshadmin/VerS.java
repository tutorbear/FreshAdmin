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
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class VerS extends AppCompatActivity {
    TextView gName,relation;
    ImageView gNId,idCardS;
    ProgressBar progress;
    ParseObject obj;
    String phone;
    static boolean confirm = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_s);
        init();
        set();
    }
    private void init() {
        obj= getIntent().getParcelableExtra("obj");
        //TextViews
        gName=findViewById(R.id.gName);
        relation=findViewById(R.id.relation);
        //Image Vies
        gNId = findViewById(R.id.nIdSPR);
        idCardS = findViewById(R.id.idCardSPR);

        //progress bar
        progress = findViewById(R.id.progressBarSPR);
    }
    private void set() {
        // Text Views
        gName.setText("Guardians Name: "+obj.getString("guardianName"));
        relation.setText("Relation: "+obj.getString("relation"));

        //Images
        HashMap <String,String> params = new HashMap<>();
        params.put("username",obj.getString("username"));
        progress.setVisibility(View.VISIBLE);
        ParseCloud.callFunctionInBackground("fetchPhotoS", params, new FunctionCallback<ArrayList>() {
            @Override
            public void done(ArrayList strings, ParseException e) {
                if (e == null) {
                    byte[] nId = Base64.decode(strings.get(0).toString(), Base64.DEFAULT);
                    byte[] studentId = Base64.decode(strings.get(1).toString(), Base64.DEFAULT);
                    phone = (String)strings.get(2);
                    Glide.with(
                            VerS.this).load(nId).into(gNId);
                    Glide.with(
                            VerS.this).load(studentId).into(idCardS);
                } else {
                    Toast.makeText(VerS.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progress.setVisibility(View.GONE);
            }
        });
    }



    public void verifyS(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            obj.put("verified",true);
            obj.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(VerS.this, "Done", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(VerS.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    public void callP(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    public void rejectS(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure ?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                obj.put("verFailed", true);
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
