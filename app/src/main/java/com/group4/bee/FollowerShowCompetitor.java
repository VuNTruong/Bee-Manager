package com.group4.bee;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowerShowCompetitor extends AppCompatActivity {

    FirebaseFirestore database = FirebaseFirestore.getInstance();

    RecyclerViewAdapterFollowCompetitor adapter;

    // Follower objects
    public ArrayList<UserClass> followerObjectArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_show);

        getData(UserProfile.selectedUserEmail);

    }

    private void getData (String targetUserEmail) {
        DocumentReference documentReference;

        documentReference = database.collection("user").document(targetUserEmail);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final HashMap<String, Object> followers = (HashMap<String, Object>) documentSnapshot.get("follower");

                        // ArrayList which contains email of followers
                        final ArrayList<String> followersEmail = new ArrayList<>();

                        for (int i = 0; i < followers.size(); i++) {
                            followersEmail.add(followers.get("user" + (i+1)).toString());
                        }

                        database.collection("user")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                if (followersEmail.contains(document.getId())) {
                                                    followerObjectArray.add(new UserClass(document.getString("firstname") + " " + document.getString("middlename") + " " + document.getString("lastname"), document.getId(),
                                                            document.getString("avatarURL")));
                                                }
                                            }

                                            System.out.println("Num of followers" + followerObjectArray.size());

                                            final RecyclerView recyclerView = findViewById(R.id.followerList);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(FollowerShowCompetitor.this));
                                            adapter = new RecyclerViewAdapterFollowCompetitor(followerObjectArray, FollowerShowCompetitor.this);

                                            recyclerView.setAdapter(adapter);
                                        } else {
                                            Log.d("Message", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
