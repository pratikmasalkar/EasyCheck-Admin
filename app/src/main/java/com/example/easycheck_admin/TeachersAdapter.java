package com.example.easycheck_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.ViewHolder> {

    Context context;
    List<Teacher> teacherList;

    public TeachersAdapter(Context context, List<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
    }
    @NonNull
    @Override
    public TeachersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersAdapter.ViewHolder holder, int position) {
        if (teacherList != null && teacherList.size() > 0) {
            Teacher teacher = teacherList.get(position);
            holder.roll_tv.setText(teacher.getName());
            holder.name_tv.setText(teacher.getCourse());
            holder.mobile_tv.setText(teacher.getMobile());
            holder.batch_tv.setText(teacher.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roll_tv, name_tv, mobile_tv, batch_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roll_tv = itemView.findViewById(R.id.roll_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            mobile_tv = itemView.findViewById(R.id.mobile_tv);
            batch_tv = itemView.findViewById(R.id.batch_tv);

        }
    }
}
