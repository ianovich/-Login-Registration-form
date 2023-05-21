package com.example.dispatch2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;// Initialize FirebaseAuth variable

    // Declare EditText, Button, and TextView variables
    private EditText signupEmail, signupPassword;

    // Override the onCreate method to define what happens when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);// Set the content view to the XML layout file for this activity

        auth = FirebaseAuth.getInstance();    // Initialize FirebaseAuth instance

        // Get references to the views defined in the XML layout file
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        Button signupButton = findViewById(R.id.signup_button);
        TextView loginRedirectText = findViewById(R.id.loginRedirectText);

        // Set an OnClickListener for the signup button
        signupButton.setOnClickListener(view -> {
            String user = signupEmail.getText().toString().trim();// Get the user's input from the EditText views
            String pass = signupPassword.getText().toString().trim();


            if (user.isEmpty()){// Validate user input
                signupEmail.setError("Email cannot be empty");
            }
            if (pass.isEmpty()){
                signupPassword.setError("Password cannot be empty");
            } else{

                // Use FirebaseAuth to create a new user with the given email and password
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // If sign up is successful, display a success message and start the login activity
                        Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {

                        // If sign up fails, display an error message with the reason for failure
                        Toast.makeText(SignUpActivity.this, "SignUp Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loginRedirectText.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }
}