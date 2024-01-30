package com.example.easycheck_admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewBatchList extends Fragment {
    private Spinner courseFilterSpinner;
    private ListView batchListView;
    private List<String> batchNames = new ArrayList<>();
    private ArrayAdapter<String> batchAdapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_batch_list, container, false);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference coursesRef = databaseReference.child("courses");
        courseFilterSpinner = view.findViewById(R.id.courseFilterSpinner);
        batchListView = view.findViewById(R.id.batchList);
        batchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, batchNames);
        batchListView.setAdapter(batchAdapter);
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
                courseFilterSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch batches from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("batches");
        fetchBatches();

        // Handle clicks on batch names
        // ...

        batchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedBatchName = batchNames.get(position);

                // Retrieve batch details from Firebase based on the name
                DatabaseReference batchRef = databaseReference.child("batches").child(clickedBatchName);
                batchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String selectedBatchName = batchNames.get(position);

                        // Create a bundle to pass batch details to the fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("batch", selectedBatchName);

                        // Create and open the BatchDetailFragment
                        BatchDetailInfoFragment batchDetailInfoFragment = new BatchDetailInfoFragment();
                        batchDetailInfoFragment.setArguments(bundle);

                        // Use a FragmentManager to manage fragment transactions
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.view_pager, batchDetailInfoFragment); // Replace with your fragment container ID
                        fragmentTransaction.addToBackStack(null); // Add to back stack for navigation
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    private void fetchBatches() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                batchNames.clear();
                for (DataSnapshot batchSnapshot : snapshot.getChildren()) {
                    Batch batch1 = batchSnapshot.getValue(Batch.class);
                    batchNames.add(batch1.getName());
                }
                batchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
