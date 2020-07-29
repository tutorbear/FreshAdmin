package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, HomePage.class));
        }
    }

    public void login(View view) {
        ParseUser.logInInBackground("taz", "1", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                } else {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void get(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");

        // The query will search for a ParseObject, given its objectId.
        // When the query finishes running, it will invoke the GetCallback
        // with either the object, or the exception thrown
        query.getInBackground("Eg6YIOqf4w", new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {
                    ArrayList<String> x =new ArrayList<>(Arrays.asList("","",""));
                    x.set(0,"jerry");
                    result.remove("interviewTime");
                    result.addAll("interviewTime",x);
                    x.set(1,"Tom");
                    result.remove("interviewTime");
                    result.addAll("interviewTime",x);

                    result.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
