package com.example.easycheck_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    private String email, password;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                loginUser(email, password);
            }
        });
    }

    private void loginUser(String email, String password) {

        // Validate input (add validation logic here)
        if (email.isEmpty()) {
            etEmail.setError("Enter Email");
            Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Enter Email");
            Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return;
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Authentication successful, proceed to Welcome Activity
                            Toast.makeText(LoginActivity.this,"Login Sucessfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, Dashboard.class));
                            finish();
                        } else {
                            String error = task.getException().getMessage().toString();
                            if (error.toLowerCase().contains("password")) {
                                etPassword.setError("Wrong Password");
                            } else if (error.toLowerCase().contains("email")) {
                                etEmail.setError("Invalid Email Address");
                            } else {
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


    }


    public void register(View view) {
        startActivity(new Intent(this, Registration.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}