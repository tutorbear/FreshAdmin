package com.example.freshadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InterviewCancelList extends AppCompatActivity {

    ArrayList<ParseObject> list;
    InterviewCancelAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_cancel_list);
        init();
    }

    private void init() {
        list = getIntent().getParcelableArrayListExtra("objects");
        recycle = findViewById(R.id.recyclerView_interviewDay_cancel);
        manager = new LinearLayoutManager(this);
        pb = findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
    }

    public void viewI(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(this,InterviewCancel.class).putExtra("object",list.get(pos)).putExtra("pos",pos),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            int removeIndex = data.getIntExtra("pos",-1);
            list.remove(removeIndex);
            customAdapter.notifyItemRemoved(removeIndex);
            customAdapter.notifyItemRangeChanged(removeIndex, list.size());
        }
    }

    public void search(View view) {
        pb.setVisibility(View.VISIBLE);
        String number = ((TextView)findViewById(R.id.number)).getText().toString();
        HashMap<String,String> params = new HashMap<>();
        params.put("username",number);
        ParseCloud.callFunctionInBackground("getStudentJobs", params, new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                   if( objects.isEmpty()){
                       Toast.makeText(InterviewCancelList.this, "Nothing found", Toast.LENGTH_SHORT).show();
                   }else{
                       list = (ArrayList<ParseObject>) objects;
                       customAdapter = new InterviewCancelAdapter(InterviewCancelList.this, list);
                       recycle.setAdapter(customAdapter);
                       recycle.setLayoutManager(manager);
                   }
                }else{
                    Toast.makeText(InterviewCancelList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(View.GONE);
            }
        });
    }


    class InterviewCancelAdapter extends RecyclerView.Adapter<InterviewCancelAdapter.MyViewHolder> {
        Context context;
        ArrayList<ParseObject> list;

        public InterviewCancelAdapter(Context context, ArrayList<ParseObject> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_interview_day,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return  holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.button.setTag(position);
            if(list.get(position).containsKey("interviewDate"))
                holder.textView.setText(""+list.get(position).getDate("interviewDate"));
            else
                holder.textView.setText("No interview Date Set yet");
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
                textView = itemView.findViewById(R.id.txt_interDay);
                button = itemView.findViewById(R.id.btn_interDay);
            }
        }
    }
}