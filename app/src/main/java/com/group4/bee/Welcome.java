package com.group4.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group4.bee.MainActivity;
import com.group4.bee.R;

public class Welcome extends AppCompatActivity {

    FirebaseFirestore database;
    private EditText userNameField;
    private EditText passwordField;
    public static FirebaseUser user;
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        database = FirebaseFirestore.getInstance();

        passwordField = findViewById(R.id.passwordField);
        userNameField = findViewById(R.id.userNameField);

        mAuth = FirebaseAuth.getInstance();

        // Handle event for the login button
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String userEmail = mAuth.getCurrentUser().getEmail();
                            database.collection("admins").document(userEmail)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                            user = mAuth.getCurrentUser();
                            Welcome.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    public void login () {
        signIn(userNameField.getText().toString(), passwordField.getText().toString());
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = userNameField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userNameField.setError("Required.");
            valid = false;
        } else {
            userNameField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }
}
