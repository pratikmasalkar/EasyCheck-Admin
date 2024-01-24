package com.example.easycheck_admin;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listBatches;
    private List<String> batchNames = new ArrayList<>();

    private Button student, courses;


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
}