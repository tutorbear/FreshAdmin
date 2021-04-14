package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class PendingJobList extends AppCompatActivity {

    PendingJobListAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_job_list);
        init();
        query();
    }
    private void init() {
        recycle = findViewById(R.id.verS_recycle);
        manager = new LinearLayoutManager(this);
    }

    private void query() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PendingJobs");
        query.include("createdBy");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if (objects.size()==0){
                        Toast.makeText(PendingJobList.this, "Nothing found", Toast.LENGTH_SHORT).show();
                    }else{
                        obj = objects;
                        customAdapter = new PendingJobListAdapter(PendingJobList.this, objects);
                        recycle.setAdapter(customAdapter);
                        recycle.setLayoutManager(manager);
                    }
                }else{
                    Toast.makeText(PendingJobList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void viewT(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(this, PendingJob.class).putExtra("obj",obj.get(pos)).putExtra("pos",pos),1);
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

class PendingJobListAdapter extends RecyclerView.Adapter<PendingJobListAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> title;

    public PendingJobListAdapter(Context context, List<ParseObject> title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View  view = inflater.inflate(R.layout.item_ver_s,parent,false);
        MyViewHolder  holder = new MyViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.button.setTag(position);
        holder.textView.setText(title.get(position).getObjectId());
    }

    @Override
    public int getItemCount() {
        return title.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.verS_username);
            button =itemView.findViewById(R.id.verS_view);
        }
    }
}