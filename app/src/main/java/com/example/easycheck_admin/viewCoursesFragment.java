package com.example.easycheck_admin;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewCoursesFragment extends Fragment {

    private ListView coursesListView;
    private List<String> courseNames;
    private FirebaseDatabase database;
    private DatabaseReference coursesRef;
    private Context context;

    public viewCoursesFragment() {
        // Required empty public constructor
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context; // Store the Context
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_courses, container, false);

        coursesListView = view.findViewById(R.id.courses_list_view);
        courseNames = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        coursesRef = database.getReference("courses");

        // Retrieve course names from Realtime Database and populate list
        coursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courseNames.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String courseName = childSnapshot.child("fname").getValue(String.class);
                    courseNames.add(courseName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, courseNames); // Use stored context
                coursesListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database errors
                Toast.makeText(getContext(), "Failed to retrieve courses: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for course items
        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCourseName = courseNames.get(position);

                // Open CourseDetailsFragment with selected course name as argument
                CourseDetailsFragment courseDetailsFragment = new CourseDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("courseName", selectedCourseName);
                courseDetailsFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.view_pager, courseDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
