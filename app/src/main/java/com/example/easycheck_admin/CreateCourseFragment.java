package com.example.easycheck_admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateCourseFragment extends Fragment {
    private EditText nameEditText;
    private EditText courseNameEditText;
    private EditText durationYearsEditText;
    private EditText totalSemestersEditText;
    private EditText infoEditText;
    private Button createCourseButton;

    private FirebaseDatabase database;
    private DatabaseReference coursesRef;




    public CreateCourseFragment() {

    }


    public static CreateCourseFragment newInstance(String param1, String param2) {
        CreateCourseFragment fragment = new CreateCourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_course_fragment, container, false);

        nameEditText=view.findViewById(R.id.name);
        courseNameEditText = view.findViewById(R.id.edit_course_name);
        durationYearsEditText = view.findViewById(R.id.edit_duration_years);
        totalSemestersEditText = view.findViewById(R.id.edit_total_semesters);
        infoEditText=view.findViewById(R.id.info);
        createCourseButton = view.findViewById(R.id.createCourseButton);

        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");
        createCourseButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String courseName = courseNameEditText.getText().toString();
            String durationYearsString = durationYearsEditText.getText().toString();
            String totalSemestersString = totalSemestersEditText.getText().toString();
            String info = infoEditText.getText().toString();

            // Validate input fields
            if (name.isEmpty() || courseName.isEmpty() || durationYearsString.isEmpty()|| totalSemestersString.isEmpty()||info.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            int durationYears = Integer.parseInt(durationYearsString);
            int totalSemesters = Integer.parseInt(totalSemestersString);
            // Create course object and push to Realtime Database
            Course course = new Course(name,courseName, durationYears, totalSemesters,info);
            coursesRef.push().setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(getContext(), "Course created successfully!", Toast.LENGTH_SHORT).show();
                    // Clear input fields after creating course
                    nameEditText.setText("");
                    courseNameEditText.setText("");
                    durationYearsEditText.setText("");
                    totalSemestersEditText.setText("");
                    infoEditText.setText("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to create course: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private static class Course {
        private String fname;
        private String name;
        private int durationYears;
        private int totalSemesters;
        private String info;

        public Course(String name, String fcourseName, int durationYears, int totalSemesters, String info) {
            this.fname = fcourseName;
            this.name = name;
            this.durationYears = durationYears;
            this.totalSemesters = totalSemesters;
            this.info = info;
        }

        // Getters
        public String getFname() {
            return fname;
        }

        public String getName() {
            return name;
        }

        public int getDurationYears() {
            return durationYears;
        }

        public int getTotalSemesters() {
            return totalSemesters;
        }

        public String getInfo() {
            return info;
        }

        // Setters
        public void setFname(String fname) {
            this.fname = fname;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDurationYears(int durationYears) {
            this.durationYears = durationYears;
        }

        public void setTotalSemesters(int totalSemesters) {
            this.totalSemesters = totalSemesters;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}