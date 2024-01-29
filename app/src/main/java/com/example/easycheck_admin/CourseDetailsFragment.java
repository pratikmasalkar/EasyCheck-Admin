package com.example.easycheck_admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseDetailsFragment extends Fragment {

    private TextView courseNameTextView, coursee_name;
    private TextView courseDescriptionTextView;
    private TextView course_duration_text_view;
    private TextView course_semesters_text_view;
    private DatabaseReference coursesRef;
    private String selectedCourseName;
    private String selectedCourseId;
    private Button removeCourseButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            selectedCourseName = args.getString("courseName");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            coursesRef = database.getReference("courses");

            coursesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String courseName = childSnapshot.child("fname").getValue(String.class);
                        selectedCourseId = childSnapshot.getKey();
                        if (courseName.equals(selectedCourseName)) {
                            String cname = childSnapshot.child("name").getValue().toString();
                            String courseDuration = childSnapshot.child("durationYears").getValue().toString();
                            String courseSemesters = childSnapshot.child("totalSemesters").getValue().toString();
                            String courseDescription = childSnapshot.child("info").getValue(String.class);
                            coursee_name.setText(cname);
                            courseNameTextView.setText(courseName);
                            courseDescriptionTextView.setText(courseDescription);
                            course_duration_text_view.setText(courseDuration + " Years");
                            course_semesters_text_view.setText(courseSemesters);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database errors
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_details, container, false);

        // Initialize your TextViews here

        courseNameTextView = view.findViewById(R.id.course_fname_text_view);
        coursee_name = view.findViewById(R.id.coursee_name);
        course_duration_text_view = view.findViewById(R.id.course_duration_text_view);
        course_semesters_text_view = view.findViewById(R.id.course_semesters_text_view);
        courseDescriptionTextView = view.findViewById(R.id.course_info_text_view);
        // ... initialize other TextViews


        removeCourseButton = view.findViewById(R.id.remove_button);
        removeCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the course from Firebase Realtime Database
                if (selectedCourseId != null) {
                    coursesRef.child(selectedCourseId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Course removed successfully
                                    Toast.makeText(getContext(), "Course removed successfully!", Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.popBackStack();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Failed to remove course: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle case where selected course name is not available
                    Toast.makeText(getContext(), "Course details not available. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
