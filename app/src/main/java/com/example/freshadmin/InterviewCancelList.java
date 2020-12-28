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

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class InterviewCancelList extends AppCompatActivity {

    ArrayList<ParseObject> list;
    InterviewCancelAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;

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

        customAdapter = new InterviewCancelAdapter(this, list);
        recycle.setAdapter(customAdapter);
        recycle.setLayoutManager(manager);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeacherProfile");
//        query.whereEqualTo("")
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
                textView = itemView.findViewById(R.id.txt_interDay);
                button = itemView.findViewById(R.id.btn_interDay);
            }
        }
    }
}