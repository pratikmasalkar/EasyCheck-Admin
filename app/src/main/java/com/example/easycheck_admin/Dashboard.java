package com.example.easycheck_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listBatches;
    private List<String> batchNames = new ArrayList<>();

    private Button student, courses;
    private TextView userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        listBatches = findViewById(R.id.list_batches);

        student = findViewById(R.id.student);
        courses = findViewById(R.id.courses);

        // Static data for now (replace with Firebase data later)
        batchNames.add("Batch A");
        batchNames.add("Batch B");
        batchNames.add("Batch C");
        batchNames.add("Batch D");
        batchNames.add("Batch E");
        userName = findViewById(R.id.userName); // Get the TextView reference

        // Check for current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch user data from Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("admins");
            userRef.child(userId) // Directly query the user's data by UID
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String name = snapshot.child("name").getValue(String.class);
                                userName.setText(name + " !"); // Set the welcome message
                            } else {
                                // Handle the case where user data is not found
                                Toast.makeText(Dashboard.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Dashboard.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where no user is logged in
            // (You might want to redirect to the login screen)
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, batchNames);
        listBatches.setAdapter(adapter);

        listBatches.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBatchName = (String) parent.getItemAtPosition(position);
            // Start BatchDetailActivity with selected batch name
            Intent intent = new Intent(Dashboard.this, BatchDetailActivity.class);
            intent.putExtra("batchName", selectedBatchName);
            startActivity(intent);
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, StudentActivity.class);
                startActivity(intent);
                finish();
            }
        });

        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Courses.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void signOut (View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
        // Optionally, navigate back to the login screen:
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}