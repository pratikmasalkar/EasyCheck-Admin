package com.example.easycheck_admin;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private TextView emailView;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String email = user.getEmail();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("admins");
        dbRef.child(user.getUid()).child("name").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String name = (String) task.getResult().getValue();
                // Use the name as needed
                emailView=findViewById(R.id.emailView);
                emailView.setText(name);

            } else {
                // Handle error
            }
        });


    }
}