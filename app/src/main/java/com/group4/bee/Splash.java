package com.group4.bee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Splash extends AppCompatActivity {

    public static FirebaseUser user;
    public static FirebaseAuth mAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        database = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            database.collection("user").document(mAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);

                                            // Finish the current activity so that the user won't be able to
                                            // come back to the log in screen
                                            Splash.this.finish();
                                        }
                                    },1000);
                        }
                    });

            user = mAuth.getCurrentUser();
        } else {

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), Welcome.class);
                            startActivity(intent);

                            // Finish the current activity so that the user won't be able to
                            // come back to the log in screen
                            Splash.this.finish();
                        }
                    },2000);
        }
    }
}
