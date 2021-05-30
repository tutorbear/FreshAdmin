package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    EditText username,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, HomePage.class));
        }else{
            username = findViewById(R.id.usernameLogin);
            pass = findViewById(R.id.passwordLogin);
        }

    }

    public void login(View view) {
        ParseUser.logInInBackground(username.getText().toString(), pass.getText().toString(), (user, e) -> {
            if (e == null) {
                startActivity(new Intent(MainActivity.this,HomePage.class));
            } else {
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
