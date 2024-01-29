package com.example.easycheck_admin;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    Context context;
    List<Student> studentList;

    public StudentsAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (studentList != null && studentList.size() > 0) {
            Student student = studentList.get(position);
            holder.roll_tv.setText(student.getRollNo());
            holder.name_tv.setText(student.getName());
            holder.mobile_tv.setText(student.getMobile());
            holder.batch_tv.setText(student.getBatch());
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
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
