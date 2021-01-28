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

public class PreLockedAdapter extends RecyclerView.Adapter<PreLockedAdapter.MyViewHolder>  {


    Context context;
    List<ParseObject> objs;

    public PreLockedAdapter(Context context, List<ParseObject> objs) {
        this.context = context;
        this.objs = objs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.items_prelocked,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final PreLocked activity = (PreLocked)context;
        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.repost(position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.delete(position);
            }
        });


        holder.postNumber.setText(objs.get(position).getString("postId"));
    }

    @Override
    public int getItemCount() {
        return objs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView postNumber;
        Button repost,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            postNumber = itemView.findViewById(R.id.postN);
            repost = itemView.findViewById(R.id.repost);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}