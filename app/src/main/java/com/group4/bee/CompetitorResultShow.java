package com.group4.bee;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompetitorResultShow extends AppCompatActivity {

    FirebaseFirestore database;
    private RecyclerViewAdapterCompetitorResult adapter;

    private ArrayList<String> startTime;
    private ArrayList<String> endTime;
    private ArrayList<String> durations;
    private ArrayList<String> scores;
    private ArrayList<String> testNumbers;
    private TextView totalTestScoreCompetitor;

    @Override
    public void onBackPressed () {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_result_show);

        database = FirebaseFirestore.getInstance();

        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
        durations = new ArrayList<>();
        scores = new ArrayList<>();
        testNumbers = new ArrayList<>();
        totalTestScoreCompetitor = findViewById(R.id.totalTestScoreCompetitor);

        getDataHistory();
    }

    private void getDataHistory() {
        String selectedUserEmail;

        if (RecyclerViewAdapterLeaderboard.selectedUserEmail == null) {
            selectedUserEmail = RecyclerViewAdapterFollower.selectedUserEmail;
        } else {
            selectedUserEmail = RecyclerViewAdapterLeaderboard.selectedUserEmail;
        }

        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = database.collection("userattempts")
                .document(selectedUserEmail);

        System.out.println(RecyclerViewAdapterLeaderboard.selectedUserEmail);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int numOfItems;

                        Map<String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("Test result");

                        numOfItems = map.size();

                        Log.d("Message", "" + numOfItems);

                        // If there's nothing in the database for this, show the user that there's nothing to show
                        if (numOfItems == 0) {
                            setContentView(R.layout.activity_nothing_here_yet);
                            return;
                        }

                        for (int i = 0; i < numOfItems; i++) {
                            Map<String, Object> map1;
                            map1 = (HashMap) map.get("test" + (i + 1));

                            durations.add(map1.get("Duration").toString());
                            endTime.add(map1.get("End time").toString());
                            scores.add(map1.get("Score").toString());
                            startTime.add(map1.get("Start time").toString());
                            testNumbers.add("TEST " + (i + 1));
                        }

                        // Set up the RecyclerView to show the detail test score
                        RecyclerView recyclerView = findViewById(R.id.competitorResults);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CompetitorResultShow.this));
                        adapter = new RecyclerViewAdapterCompetitorResult(startTime, endTime, scores, durations, testNumbers,CompetitorResultShow.this);
                        recyclerView.setAdapter(adapter);

                        // Show the total test score
                        totalTestScoreCompetitor.setText(documentSnapshot.getString("Score to display"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
