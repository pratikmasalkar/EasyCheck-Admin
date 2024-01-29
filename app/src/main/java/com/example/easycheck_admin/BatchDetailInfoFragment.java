package com.example.easycheck_admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.Map;

public class BatchDetailInfoFragment extends Fragment {
    private String selectedBatchName, selectedBatchId;
    private Button removeBatchButton;
    private DatabaseReference batchRef;
    private TextView tvBatchId, tvBatchName, tvCourse, tvSubjects, tvTeachers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_batch_detail_info, container, false);

        tvBatchName = view.findViewById(R.id.tv_batch_name);
        tvCourse = view.findViewById(R.id.tv_course);
        tvTeachers = view.findViewById(R.id.tv_teachers);
        Bundle args = getArguments();
        if (args != null) {
            selectedBatchName = args.getString("batch");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            batchRef = database.getReference("batches");
            batchRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String batchName = childSnapshot.child("name").getValue(String.class);
                        selectedBatchId = childSnapshot.getKey();
                        if (batchName.equals(selectedBatchName)) {
                            String name = childSnapshot.child("name").getValue().toString();
                            String course = childSnapshot.child("course").getValue().toString();
                            Map<String, String> subjects = (Map<String, String>) childSnapshot.child("subjects").getValue();
                            Map<String, String> teachers = (Map<String, String>) childSnapshot.child("teachers").getValue();
                            Batch batch = new Batch(name, course, subjects, teachers);

                            tvBatchName.setText(batch.getName());
                            tvCourse.setText(batch.getCourse());
                            tvTeachers.setText(batch.getTeachersAsString());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG);
                }
            });
            // Assuming a method to format teachers
        }

        removeBatchButton = view.findViewById(R.id.remove_button);
        removeBatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the course from Firebase Realtime Database
                if (selectedBatchId != null) {
                    batchRef.child(selectedBatchId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Course removed successfully
                                    Toast.makeText(getContext(), "Batch removed successfully!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Batch details not available. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}