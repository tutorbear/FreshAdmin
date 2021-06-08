package com.example.freshadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserPosts extends AppCompatActivity {
    AdminPostsAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);
        init();
        query();
    }

    private void init() {
        recycle = findViewById(R.id.recycle_admin);
        manager = new LinearLayoutManager(this);
    }

    private void query() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("createdBy");
        query.whereEqualTo("fromApp",true);
        query.whereEqualTo("locked",false);
        query.findInBackground((objects, e) -> {
            if(e==null){
                if (objects.size()==0){
                    Toast.makeText(this, "Nothing found", Toast.LENGTH_SHORT).show();
                }else{
                    obj = objects;
                    customAdapter = new AdminPostsAdapter(this, objects);
                    recycle.setAdapter(customAdapter);
                    recycle.setLayoutManager(manager);
                }
            }else{
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}