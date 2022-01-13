package com.example.freshadmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freshadmin.databinding.ItemStudentTeachersBinding;
import com.parse.ParseObject;

import java.util.List;

public class StudentTeacherApplicationsAdapter extends RecyclerView.Adapter<StudentTeacherApplicationsAdapter.MyViewHolder> {
    private Context context;
    private List<ParseObject> teacherObjects;
    private List<String> pictures;
    private boolean clear;

    public StudentTeacherApplicationsAdapter(Context context, List<Object> objects) {
        this.context = context;
        teacherObjects = (List<ParseObject>) objects.get(0);

        pictures = (List<String>) objects.get(1);

    }


    public void fuckYou(){
        clear = true;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MyViewHolder(ItemStudentTeachersBinding.inflate(inflater,parent,false));
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public ItemStudentTeachersBinding binding;

        MyViewHolder(@NonNull ItemStudentTeachersBinding b) {
            super(b.getRoot());
            binding = b;

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //TODO don't show full name ,
        holder.binding.txtName.setText(teacherObjects.get(position).getString("fullName"));
        int i = 1;
        if (teacherObjects.get(position).getString("curriculum").equals("Bengali Medium")
                || teacherObjects.get(position).getString("curriculum").equals("Madrasa Medium")
                || teacherObjects.get(position).getString("curriculum").equals("English-Version")
        ) {
            i = 0;
        }

        //UNI
        if (teacherObjects.get(position).containsKey("university"))
            holder.binding.txtUniNameDegree.setText(teacherObjects.get(position).getString("uniProgram") + " from " + teacherObjects.get(position).getString("university").toUpperCase());
        else
            holder.binding.txtUniNameDegree.setVisibility(View.GONE);
        //COLLEGE
        if (teacherObjects.get(position).containsKey("college"))
            if (i == 1)
                holder.binding.txtCollegeNameDegree.setText("A Level from " + teacherObjects.get(position).getString("college"));
            else
                holder.binding.txtCollegeNameDegree.setText("HSC from " + teacherObjects.get(position).getString("college"));
        else
            holder.binding.txtCollegeNameDegree.setVisibility(View.GONE);
        //SCHOOL
        if (i == 1)
            holder.binding.txtSchoolNameDegree.setText("O Level from " + teacherObjects.get(position).getString("school"));
        else
            holder.binding.txtSchoolNameDegree.setText("SSC from " + teacherObjects.get(position).getString("school"));


        holder.binding.txtCurriculum.setText(teacherObjects.get(position).getString("curriculum"));
        holder.binding.txtBackground.setText(teacherObjects.get(position).getString("background") + " background");
        holder.binding.txtExperience.setText("Experience: " + teacherObjects.get(position).getString("teachingExp"));


        Glide.with(
                context).load(Base64.decode(pictures.get(position), Base64.DEFAULT)).into(holder.binding.imgProPic);

        TeacherSelection x = (TeacherSelection) context;

        if(clear){
            holder.binding.selectChip.setChecked(false);
        }else{
            //Animation
            holder.binding.border.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
            holder.binding.imgProPic.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
            holder.binding.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_in_left_side));
        }

        holder.binding.selectChip.setOnClickListener(v -> {
            x.chipClicked(v,teacherObjects.get(position).getObjectId());
        });

        if(position+1==getItemCount()){
            //this was the last item
            clear = false;
        }

    }


    @Override
    public int getItemCount() {
        return teacherObjects.size();
    }


}
