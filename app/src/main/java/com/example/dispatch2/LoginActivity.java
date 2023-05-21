package com.example.dispatch2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private FirebaseAuth auth;
    TextView forgotPassword;
    GoogleSignInButton googleBtn;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find view elements by their IDs

        loginEmail = findViewById( R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signupRedirectText = findViewById(R.id.signUpRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);
        googleBtn = findViewById(R.id.googleBtn);
        auth = FirebaseAuth.getInstance();  // Get an instance of FirebaseAuth for user authentication

        // Set a click listener for the login button
        loginButton.setOnClickListener(v -> {
            // Get the email and password entered by the user
            String email = loginEmail.getText().toString();
            String pass = loginPassword.getText().toString();

            // Check if the email field is not empty and if it is a valid email address
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                if (!pass.isEmpty()) { // Check if the password field is not empty

                    // Sign in the user with email and password using FirebaseAuth
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(authResult -> {

                                // Show a toast message for successful login and start the MainActivity
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                // Show a toast message for failed login
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Show an error message for empty password field
                    loginPassword.setError("Empty fields are not allowed");
                }
            } else if (email.isEmpty()) { // Show an error message for empty email field

                loginEmail.setError("Empty fields are not allowed");
            } else {
                loginEmail.setError("Please enter correct email");// Show an error message for invalid email field

            }
        });
        // Set a click listener for the sign-up redirect text
        signupRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));// Start the SignupActivity

        });
        // Set a click listener for the forgot password text view
        forgotPassword.setOnClickListener(view -> {

            // Show an alert dialog for resetting the password
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
            EditText emailBox = dialogView.findViewById(R.id.emailBox);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();
            dialogView.findViewById(R.id.btnReset).setOnClickListener(view1 -> {
                String userEmail = emailBox.getText().toString();
                if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    // Show an error message for invalid email format
                    Toast.makeText(LoginActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(LoginActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            dialogView.findViewById(R.id.btnCancel).setOnClickListener(view12 -> dialog.dismiss());
            if (dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            dialog.show();
        });
        //Inside onCreate
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (gAccount != null){
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            task.getResult(ApiException.class);
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (ApiException e){
                            Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //OnClickListener for the "Google Sign In" button:
        googleBtn.setOnClickListener(view -> {//Define the onClick method for the OnClickListener.
            //When the button is clicked, it creates a new Intent object that starts the Google Sign-In flow:
            Intent signInIntent = gClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);//Launch the sign-in Intent using the ActivityResultLauncher (activityResultLauncher).
        });
    }
}