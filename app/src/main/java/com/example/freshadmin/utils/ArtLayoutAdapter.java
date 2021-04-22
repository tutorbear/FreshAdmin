package com.example.freshadmin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshadmin.R;
import com.example.freshadmin.databinding.ItemArtLayoutBinding;
import com.github.ybq.android.spinkit.SpinKitView;


import java.util.HashMap;

public class ArtLayoutAdapter extends RecyclerView.Adapter<ArtLayoutAdapter.ViewHolder> {
    HashMap<String, Object> params;
    Context context;
    SpinKitView spinKitView;
    public ArtLayoutAdapter(HashMap<String, Object> params) {
        this.params = params;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ArtLayoutAdapter.ViewHolder(ItemArtLayoutBinding.inflate((inflater),parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(params.containsKey("heading"))
            holder.binding.heading.setText(params.get("heading")+"");
        else
            holder.binding.heading.setVisibility(View.GONE);
        holder.binding.body.setText(params.get("body")+"");

        if(params.containsKey("button"))
            holder.binding.button.setText(params.get("button")+"");
        else
            holder.binding.button.setVisibility(View.GONE);
        holder.binding.image.setImageResource((Integer)params.get("image"));
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(context, R.anim.small_overshoot));
        spinKitView = holder.binding.spinKit;
    }

    public View getSpinKitView(){
        return spinKitView;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        ItemArtLayoutBinding binding;
        public ViewHolder(ItemArtLayoutBinding b) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(b.getRoot());
            binding = b;
        }
    }
}
