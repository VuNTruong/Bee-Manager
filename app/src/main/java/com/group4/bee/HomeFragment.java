package com.group4.bee;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private FirebaseFirestore database;
    private TextView nameTextView;
    public static String startTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        // Initialization begins
        database = FirebaseFirestore.getInstance();

        nameTextView = view.findViewById(R.id.mainMenuNameView);

        // Initialization ends

        // This is to show information of user at the beginning of page
        getUserInformation();

        // Handle event for the user manager button
        ImageButton userManager = view.findViewById(R.id.userManager);
        userManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Handle event for the quiz manager button
        ImageButton quizManager = view.findViewById(R.id.quizManager);
        quizManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Handle event for the sign out button
        ImageButton signout = view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Welcome.mAuth != null) {
                    Welcome.mAuth.signOut();
                    Intent intent = new Intent(v.getContext(), Welcome.class);
                    startActivity(intent);

                } else {
                    Splash.mAuth.signOut();
                    Intent intent = new Intent(v.getContext(), Welcome.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void getUserInformation () {

        DocumentReference documentReference;

        if (Splash.user != null) {
            documentReference = database.collection("admins").document(Splash.user.getEmail());
        } else {
            documentReference = database.collection("admins").document(Welcome.user.getEmail());
        }

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String name;

                        if (documentSnapshot.getString("middlename").equals("")) {
                            name = documentSnapshot.getString("firstname") + " " +
                                    documentSnapshot.getString("lastname");
                        } else {
                            name = documentSnapshot.getString("firstname") + " " +
                                    documentSnapshot.getString("middlename") + " " +
                                    documentSnapshot.getString("lastname");
                        }


                        nameTextView.setText(name);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}