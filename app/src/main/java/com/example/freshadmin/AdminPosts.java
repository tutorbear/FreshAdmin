package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class AdminPosts extends AppCompatActivity {
    AdminPostsAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_posts);
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
        query.whereEqualTo("fromApp",false);
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
    public void viewT(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(this, TeacherSelection.class).putExtra("id",obj.get(pos).getObjectId()).putExtra("pos",pos),1);
    }
    public void callP(View view) {
        int pos = (int) view.getTag();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+obj.get(pos).getParseObject("createdBy").get("username")));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            int removeIndex = data.getIntExtra("pos",-1);
            obj.remove(removeIndex);
            customAdapter.notifyItemRemoved(removeIndex);
            customAdapter.notifyItemRangeChanged(removeIndex, obj.size());
        }
    }
}
