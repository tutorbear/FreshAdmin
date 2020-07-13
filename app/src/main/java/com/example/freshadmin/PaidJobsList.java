package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

public class PaidJobsList extends AppCompatActivity {
    List<ParseObject> list;
    PaidJobsListAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_jobs_list);
        init();
    }

    private void init() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.whereExists("trxId");
        query.include("hired");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                list = objects;
                recycle = findViewById(R.id.recycle_PJ);
                manager = new LinearLayoutManager(PaidJobsList.this);
                customAdapter = new PaidJobsListAdapter(PaidJobsList.this, list);
                recycle.setAdapter(customAdapter);
                recycle.setLayoutManager(manager);
            }
        });

    }

    public void viewJP(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(PaidJobsList.this,PaidJob.class).putExtra("object",list.get(pos)).putExtra("pos",pos),1);

    }
}

class PaidJobsListAdapter extends RecyclerView.Adapter<PaidJobsListAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> list;


    public PaidJobsListAdapter(Context context, List<ParseObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_paid_job,parent,false);
        MyViewHolder  holder = new MyViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setTag(position);
        holder.textView.setText(list.get(position).getObjectId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_paid_job);
            button = itemView.findViewById(R.id.viewPJ);
        }
    }
}