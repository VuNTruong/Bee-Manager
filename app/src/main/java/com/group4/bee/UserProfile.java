package com.group4.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.group4.bee.UserClass;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private TextView overallScoreCompetitor;
    RecyclerView recyclerView;
    RecyclerViewAdapterSocial adapter;
    public static String selectedUserEmail;

    ArrayList<UserClass> creatorObjects = new ArrayList<>();
    ArrayList<String> postContent = new ArrayList<>();
    ArrayList<String> postID = new ArrayList<>();

    @Override
    public void onBackPressed () {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_profile);

        recyclerView = findViewById(R.id.competitorPost);

        overallScoreCompetitor = findViewById(R.id.overallScoreCompetitor);

        if (RecyclerViewAdapterLeaderboard.selectedUserEmail == null) {
            selectedUserEmail = RecyclerViewAdapterFollower.selectedUserEmail;
        } else {
            selectedUserEmail = RecyclerViewAdapterLeaderboard.selectedUserEmail;
        }

        // Setup event when the user want to know the number of follower of competitor
        TextView competitorNumOfFollower = findViewById(R.id.numOfFollowersCompetitor);
        TextView competitorNumOfFollowing = findViewById(R.id.numOfFollowingCompetitor);

        competitorNumOfFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowerShowCompetitor.class);
                startActivity(intent);
            }
        });

        competitorNumOfFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowingShowCompetitor.class);
                startActivity(intent);
            }
        });

        database.collection("user").document(selectedUserEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // Get the name component of the profile show
                        TextView nameView = findViewById(R.id.nameProfileCompetitor);
                        TextView numOfFollowers = findViewById(R.id.numOfFollowersCompetitor);
                        TextView numOfFollowings = findViewById(R.id.numOfFollowingCompetitor);
                        TextView nameViewTitle = findViewById(R.id.nameProfileCompetitorTitle);
                        CircleImageView profilePicture = findViewById(R.id.avatarShowCompetitorProfile);
                        Button showFullScore = findViewById(R.id.showFullScoreProfile);

                        nameView.setText(documentSnapshot.getString("firstname") + " "
                                + documentSnapshot.getString("middlename") + " "
                                + documentSnapshot.getString("lastname"));

                        nameViewTitle.setText(documentSnapshot.getString("firstname") + " "
                                + documentSnapshot.getString("middlename") + " "
                                + documentSnapshot.getString("lastname"));

                        HashMap <String, Object> followers = (HashMap<String, Object>) documentSnapshot.get("follower");
                        HashMap <String, Object> following = (HashMap<String, Object>) documentSnapshot.get("following");

                        Glide.with(UserProfile.this)
                                .load(documentSnapshot.getString("avatarURL"))
                                .into(profilePicture);

                        System.out.println("Number of followers " + followers.size());

                        numOfFollowers.setText("" + followers.size());
                        numOfFollowings.setText("" + following.size());

                        showFullScore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UserProfile.this, CompetitorResultShow.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        database.collection("userattempts")
                .document(selectedUserEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Set the overall score
                        overallScoreCompetitor.setText(documentSnapshot.getString("Score to display"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        getPost();
    }

    private void getPost () {
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                String userName = document.getString("creatorName");

                                String userEmail = document.getString("creatorEmail");

                                String userAvatar = document.getString("creatorAvatar");

                                if (userEmail.equals(selectedUserEmail)) {

                                    creatorObjects.add(new UserClass(userName, userEmail, userAvatar));

                                    postContent.add(document.getString("content"));

                                    postID.add(document.getId());
                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(UserProfile.this));
                                adapter = new RecyclerViewAdapterSocial(reverseArrayListUserClass(creatorObjects),
                                        reverseArrayListString(postContent), reverseArrayListString(postID), UserProfile.this);
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d("Message", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public ArrayList<String> reverseArrayListString(ArrayList<String> originalArray)
    {
        // Arraylist for storing reversed elements
        ArrayList<String> revArrayList = new ArrayList<String>();
        for (int i = originalArray.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(originalArray.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }

    public ArrayList<UserClass> reverseArrayListUserClass(ArrayList<UserClass> originalArray) {
        // Arraylist for storing reversed elements
        ArrayList<UserClass> revArrayList = new ArrayList<UserClass>();
        for (int i = originalArray.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(originalArray.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }
}
