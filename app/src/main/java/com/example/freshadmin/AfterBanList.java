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

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AfterBanList extends AppCompatActivity {
    List<ParseObject> list;
    AfterBanListAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_list);
        init();
    }

    private void init() {

        // Your query:
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("createdBy.sProfile");
        query.include("requested");
        query.whereNotEqualTo("paymentDate",null);
        query.whereEqualTo("hired",null);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if(!objects.isEmpty()){
                        list =  objects;
                        recycle = findViewById(R.id.recyclerViewUP);
                        manager = new LinearLayoutManager(AfterBanList.this);
                        customAdapter = new AfterBanListAdapter(AfterBanList.this, list);
                        recycle.setAdapter(customAdapter);
                        recycle.setLayoutManager(manager);
                    }else{
                        Toast.makeText(AfterBanList.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AfterBanList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void viewJP(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(AfterBanList.this, AfterBan.class).putExtra("object",list.get(pos)),1);
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
}


class AfterBanListAdapter extends RecyclerView.Adapter<AfterBanListAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> list;


    public AfterBanListAdapter(Context context, List<ParseObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_unpaid,parent,false);
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
            textView = itemView.findViewById(R.id.txt_unpaid);
            button = itemView.findViewById(R.id.viewUP);
        }
    }
}
