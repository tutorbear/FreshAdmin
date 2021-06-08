package com.example.freshadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.List;

class AdminPostsAdapter extends RecyclerView.Adapter<AdminPostsAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> obj;

    public AdminPostsAdapter(Context context, List<ParseObject> obj) {
        this.context = context;
        this.obj = obj;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_admin_post,parent,false);
        MyViewHolder  holder = new MyViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.call.setTag(position);
        holder.view.setTag(position);
        holder.nego.setText("Nego: "+ obj.get(position).getBoolean("negotiable"));
        holder.id.setText("ID: "+ obj.get(position).getObjectId());
        holder.name.setText("Name: "+obj.get(position).getParseObject("createdBy").getString("guardianName"));
        holder.curr.setText("Cur: "+obj.get(position).getString("curriculum"));
        holder.email.setText(""+obj.get(position).getParseObject("createdBy").getString("email"));
        holder.salary.setText("Salary: "+ obj.get(position).get("salary").toString());
        holder.location.setText("Location: "+ obj.get(position).getString("location"));
        holder.stdNumber.setText("Number: "+ obj.get(position).get("numberOfStudents").toString());
        holder.sClass.setText("Class: "+ obj.get(position).getString("class1")+","+ obj.get(position).getString("class2"));
        holder.sub.setText("Subject1: "+ obj.get(position).getString("subject1")+"\nSubject2: "+ obj.get(position).getString("subject2"));
        holder.address.setText("Address: "+ obj.get(position).getString("address"));
        holder.count.setText("Applied Num: "+ obj.get(position).getNumber("appliedCount"));
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView email,id,nego,name,salary,location,stdNumber,sClass,sub,curr,address,count;
        Button call,view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Text views
            id = itemView.findViewById(R.id.jobIdJP);
            name = itemView.findViewById(R.id.nameJP);
            salary = itemView.findViewById(R.id.salaryJP);
            location = itemView.findViewById(R.id.locationJP);
            stdNumber = itemView.findViewById(R.id.studentNumberJP);
            sClass = itemView.findViewById(R.id.classJP);
            sub = itemView.findViewById(R.id.subjectJP);
            curr = itemView.findViewById(R.id.curriculumJP);
            email = itemView.findViewById(R.id.email);
            address = itemView.findViewById(R.id.addressJP);
            count = itemView.findViewById(R.id.appliedCount);
            nego = itemView.findViewById(R.id.negoJP);
            call = itemView.findViewById(R.id.callP);
            view = itemView.findViewById(R.id.view_job);
        }
    }
}