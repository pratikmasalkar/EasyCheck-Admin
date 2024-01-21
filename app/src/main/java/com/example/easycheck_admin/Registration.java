package com.example.easycheck_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private EditText etName, etEmail, etPassword, etCnfpass;
    private Button registerButton;
    private FirebaseAuth auth;
    private DatabaseReference adminRef;

    private String name, email, password, cnfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etCnfpass = findViewById(R.id.etCnfpass);

        auth = FirebaseAuth.getInstance();
        adminRef = FirebaseDatabase.getInstance().getReference("admins");

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAdmin();
            }
        });
    }

    private void registerAdmin() {
        this.name = etName.getText().toString();
        this.email = etEmail.getText().toString();
        this.cnfPassword = etCnfpass.getText().toString();
        this.password = etPassword.getText().toString();

        // Input validation
        if (name.isEmpty() || email.isEmpty() || cnfPassword.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.length() < 3) {
            etName.setError("Name should have at least 3 characters");
            return;
        } else if (!name.matches("[a-zA-Z ]+")) {
            etName.setError("Name should only contain letters and spaces");
            return;
        }
        if (password.equals(cnfPassword)==false) {
            etCnfpass.setError("Password and Confirm Password Should Be Same");
            etPassword.setError("Password and Confirm Password Should Be Same");
        } else {
            createUserInFirebase(email, password);
        }
    }

    private void createUserInFirebase(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            saveAdminData(uid, name, email);
                            Toast.makeText(Registration.this, "Registration SuccessFully", Toast.LENGTH_LONG).show();
                            // Clear input fields
                            etName.setText("");
                            etEmail.setText("");
                            etPassword.setText("");
                            etCnfpass.setText("");

                            // Navigate to another activity
                            startActivity(new Intent(Registration.this, LoginActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                            finish();
                        } else {
                            String errorMsg = task.getException().getMessage().toString();
                            if (errorMsg.contains("password")) {
                                etPassword.setError(errorMsg);
                                etPassword.requestFocus();
                            } else if (errorMsg.contains("email")) {
                                etEmail.setError(errorMsg);
                                etEmail.requestFocus();
                            } else {
                                Toast.makeText(Registration.this, errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void saveAdminData(String uid, String name, String email) {
        try {
            Admin admin = new Admin(name, email);
            adminRef.child(uid).setValue(admin);
        } catch (Exception e) {
            Log.e("Registration", "Error saving admin data: " + e.getMessage());
            Toast.makeText(this, "Registration failed. Please check your internet connection or try again later.", Toast.LENGTH_LONG).show();
        }
    }

    public void onLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}