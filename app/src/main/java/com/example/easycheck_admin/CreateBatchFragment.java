package com.example.easycheck_admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBatchFragment extends Fragment {

    private Spinner courseSpinner;
    private LinearLayout subjectTeacherLayout;
    private EditText batchNameEditText, subject1, subject2, subject3, subject4, subject5, subject6;
    private Spinner teacher1, teacher2, teacher3, teacher4, teacher5, teacher6;
    private Button submitButton;

    private DatabaseReference databaseReference;
    private DatabaseReference teachersRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_batch, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference coursesRef = databaseReference.child("courses");
        teachersRef = databaseReference.child("teachers");

        // Initialize views
        courseSpinner = view.findViewById(R.id.courseSpinner);
        batchNameEditText = view.findViewById(R.id.batchNameEditText);
        submitButton = view.findViewById(R.id.submitButton);
        subjectTeacherLayout = view.findViewById(R.id.subjectTeacherLayout);
        subject1 = view.findViewById(R.id.subject1);
        subject2 = view.findViewById(R.id.subject2);
        subject3 = view.findViewById(R.id.subject3);
        subject4 = view.findViewById(R.id.subject4);
        subject5 = view.findViewById(R.id.subject5);
        subject6 = view.findViewById(R.id.subject6);

        teacher1 = view.findViewById(R.id.teacher1);
        teacher2 = view.findViewById(R.id.teacher2);
        teacher3 = view.findViewById(R.id.teacher3);
        teacher4 = view.findViewById(R.id.teacher4);
        teacher5 = view.findViewById(R.id.teacher5);
        teacher6 = view.findViewById(R.id.teacher6);


        // Fetch course list and populate spinner
        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> courseNames = new ArrayList<>();
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseName = courseSnapshot.child("name").getValue(String.class);
                    courseNames.add(courseName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courseNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Populate teacher spinners (implementation
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourse = parent.getItemAtPosition(position).toString();
                populateTeacherSpinners(selectedCourse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBatch();
            }
        });

        return view;
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void populateTeacherSpinners(String courseName) {
        teachersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> teacherNames = new ArrayList<>();
                for (DataSnapshot teacherSnapshot : snapshot.getChildren()) {
                    String teacherCourse = teacherSnapshot.child("course").getValue(String.class);
                    if (teacherCourse != null && teacherCourse.equals(courseName)) {
                        String teacherName = teacherSnapshot.child("name").getValue(String.class);
                        teacherNames.add(teacherName);
                    }
                }
                populateSpinner(teacher1, teacherNames);
                populateSpinner(teacher2, teacherNames);
                populateSpinner(teacher3, teacherNames);
                populateSpinner(teacher4, teacherNames);
                populateSpinner(teacher5, teacherNames);
                populateSpinner(teacher6, teacherNames);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createBatch() {
        String courseName = courseSpinner.getSelectedItem().toString();
        String batchName = batchNameEditText.getText().toString().trim();


        Map<String, Object> batchData = new HashMap<>();
        Map<String, String> teachers = new HashMap<>();

        Map<String, String> subjects = new HashMap<>();

        View rootView = getView();
        for (int i = 1; i <= 6; i++) {
            EditText subjectEditText = rootView.findViewById(getResources().getIdentifier("subject" + i, "id", getActivity().getPackageName()));
            String subjectName = subjectEditText.getText().toString().trim();
            if (!subjectName.isEmpty()) {
                subjects.put("subject" + i, subjectName);
                Spinner teacherSpinner = rootView.findViewById(getResources().getIdentifier("teacher" + i, "id", getActivity().getPackageName()));
                teachers.put(subjectName, teacherSpinner.getSelectedItem().toString());
                batchData.put("teachers", teachers);
            }
        }

        // Create batch data structure
        batchData.put("name", batchName);
        batchData.put("course",courseName);
        batchData.put("subjects", subjects);
        batchData.put("teachers", teachers);

        // Create batch in Firebase
        databaseReference.child("batches").push().setValue(batchData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Batch created successfully!", Toast.LENGTH_SHORT).show();

                            courseSpinner.setSelection(0);
                            batchNameEditText.setText("");
                            subject1.setText("");
                            subject2.setText("");
                            subject3.setText("");
                            subject4.setText("");
                            subject5.setText("");
                            subject6.setText("");

                            teacher1.setSelection(0);
                            teacher2.setSelection(0);
                            teacher3.setSelection(0);
                            teacher4.setSelection(0);
                            teacher5.setSelection(0);
                            teacher6.setSelection(0);

                            View currentFocus = getActivity().getCurrentFocus();
                            if (currentFocus != null) {
                                currentFocus.clearFocus();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to create batch: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
