package com.example.easycheck_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private TeachersAdapter teachersAdapter;
    private DatabaseReference teacherRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        recycler_view = findViewById(R.id.recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        teacherRef = database.getReference("teachers");

        // Fetch student data from Realtime Database
        fetchTeachers();
    }


    private void setRecyclerView(List<Teacher> teacherList) {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        teachersAdapter = new TeachersAdapter(this,teacherList);
        recycler_view.setAdapter(teachersAdapter);
    }

    private void fetchTeachers() {
        List<Teacher> teacherList = new ArrayList<>();

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    Teacher teacher = childSnapshot.getValue(Teacher.class);
                    teacherList.add(teacher);
                }

                // After fetching students, set up RecyclerView
                setRecyclerView(teacherList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TeacherActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void dashboard(View view) {
        startActivity(new Intent(this, Dashboard.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}