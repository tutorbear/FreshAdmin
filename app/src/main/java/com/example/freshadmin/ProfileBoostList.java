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

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

public class ProfileBoostList extends AppCompatActivity {

    ProfileBoostAdapter customAdapter;
    LinearLayoutManager manager;
    RecyclerView recycle;
    List<ParseObject> obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_jobs_list);
        init();
        query();
    }

    private void init() {
        recycle = findViewById(R.id.locked_recycle);
        manager = new LinearLayoutManager(this);
    }

    private void query() {
        ParseCloud.callFunctionInBackground("fetchBoost", new HashMap<>(), (FunctionCallback<List<ParseObject>>) (objects, e) -> {
            if (e == null) {
                if (!objects.isEmpty()) {
                    obj = objects;
                    customAdapter = new ProfileBoostAdapter(ProfileBoostList.this, objects);
                    recycle.setAdapter(customAdapter);
                    recycle.setLayoutManager(manager);
                } else {
                    Toast.makeText(this, "Nothing Found", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewL(View view) {
        int pos = (int) view.getTag();
        startActivityForResult(new Intent(this, VerifyBoost.class).putExtra("obj", obj.get(pos)).putExtra("pos", pos), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int removeIndex = data.getIntExtra("pos", -1);
            obj.remove(removeIndex);
            customAdapter.notifyItemRemoved(removeIndex);
            customAdapter.notifyItemRangeChanged(removeIndex, obj.size());
        }
    }
}

class ProfileBoostAdapter extends RecyclerView.Adapter<ProfileBoostAdapter.MyViewHolder> {

    Context context;
    List<ParseObject> title;

    public ProfileBoostAdapter(Context context, List<ParseObject> title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_locked_jobs, parent, false);
        return new MyViewHolder(view);
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


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.locked_job_id);
            button = itemView.findViewById(R.id.locked_view);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}