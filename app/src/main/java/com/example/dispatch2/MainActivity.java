package com.example.dispatch2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
public class MainActivity extends AppCompatActivity {
    TextView userName;// Declare a TextView variable named userName
    Button logout;// Declare a Button variable named logout
    GoogleSignInClient gClient;// Declare a GoogleSignInClient variable named gClient
    GoogleSignInOptions gOptions;// Declare a GoogleSignInOptions variable named gOptions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the content view to activity_main.xml layout

        logout = findViewById(R.id.logout);// Find the Button with id logout from activity_main.xml layout and assign it to logout variable

        userName = findViewById(R.id.userName);// Find the TextView with id userName from activity_main.xml layout and assign it to userName variable

        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gClient = GoogleSignIn.getClient(this, gOptions);// Build the GoogleSignInClient with this activity and the GoogleSignInOptions

        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);// Build the GoogleSignInClient with this activity and the GoogleSignInOptions

        if (gAccount != null){
            String gName = gAccount.getDisplayName(); // Get the display name of the signed-in account

            userName.setText(gName);// Set the text of userName TextView with the display name
        }
        // Add a click listener to logout Button
        // Sign out from GoogleSignInClient and add a listener to handle the completion of the task
        logout.setOnClickListener(view -> gClient.signOut().addOnCompleteListener(task -> {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));// Start LoginActivity
        }));
    }
}