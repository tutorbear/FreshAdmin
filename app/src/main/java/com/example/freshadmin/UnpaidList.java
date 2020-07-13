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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class UnpaidList extends AppCompatActivity {
    List<ParseObject> list;
    UnpaidListAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_list);
        init();
    }

    private void init() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // start of today
        Date today = cal.getTime();

        // start of tomorrow
        cal.add(Calendar.DAY_OF_MONTH, 1); // add one day to get start of tomorrow
        Date tomorrow = cal.getTime();


        // Your query:
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Unpaid");
        query.whereGreaterThan("date", today);
        query.whereLessThan("date", tomorrow);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if(!objects.isEmpty()){
                        list =  objects;
                        recycle = findViewById(R.id.recyclerViewUP);
                        manager = new LinearLayoutManager(UnpaidList.this);
                        customAdapter = new UnpaidListAdapter(UnpaidList.this, list);
                        recycle.setAdapter(customAdapter);
                        recycle.setLayoutManager(manager);
                    }else{
                        Toast.makeText(UnpaidList.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UnpaidList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void viewJP(View view) {
        int pos = (int) view.getTag();
//        Toast.makeText(this, ""+pos, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UnpaidList.this,Unpaid.class).putExtra("object",list.get(pos)));

    }
}

class UnpaidListAdapter extends RecyclerView.Adapter<UnpaidListAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> list;


    public UnpaidListAdapter(Context context, List<ParseObject> list) {
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
