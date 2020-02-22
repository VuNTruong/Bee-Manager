package com.group4.bee;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private ImageView sendButton;
    private EditText commentEdit;
    RecyclerView recyclerView;
    RecyclerViewAdapterSocialComment adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> creatorName = new ArrayList<>();
    ArrayList<String> commentContent = new ArrayList<>();

    @Override
    public void onBackPressed () {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = CommentActivity.this.findViewById(R.id.commentsView);

        sendButton = findViewById(R.id.sendButton);
        commentEdit = findViewById(R.id.commentEdit);

        // Call the function to display the comment
        getComments();

        // Set up event for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViewsInLayout();
                try {
                    adapter.clear();
                    postComment();
                } catch (NullPointerException ex) {
                    postComment();
                }
            }
        });

        // Set up action for the swipe refresh
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.removeAllViewsInLayout();
                try {
                    adapter.clear();
                    getComments();
                } catch (NullPointerException ex) {
                    getComments();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getComments () {
        database.collection("posts").document(RecyclerViewAdapterSocial.selectedPostID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // The map which contains all comments
                        HashMap<String, Object> comments = (HashMap<String, Object>) documentSnapshot.get("comments");

                        for (int i = 0; i < comments.size(); i++) {

                            // The map which contains a comment
                            HashMap<String, Object> comment;

                            comment = (HashMap<String, Object>) comments.get("comment" + (i + 1));

                            creatorName.add(comment.get("creator").toString());
                            commentContent.add(comment.get("content").toString());

                            // Create the RecyclerView
                            recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
                            adapter = new RecyclerViewAdapterSocialComment(creatorName, commentContent, CommentActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void postComment () {
        database.collection("posts").document(RecyclerViewAdapterSocial.selectedPostID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final HashMap<String, Object> comments = (HashMap<String, Object>) documentSnapshot.get("comments");

                        // Map which hold information for the new comment
                        final HashMap<String, Object> newComment = new HashMap<>();

                        DocumentReference documentReference;

                        if (Welcome.user != null) {
                            documentReference = database.collection("user").document(Welcome.user.getEmail());
                        } else {
                            documentReference = database.collection("user").document(Splash.user.getEmail());
                        }

                        documentReference.get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String userName = documentSnapshot.getString("firstname") + " "
                                                + documentSnapshot.getString("middlename") + " "
                                                + documentSnapshot.getString("lastname");

                                        newComment.put("content", commentEdit.getText().toString());
                                        newComment.put("creator", userName);

                                        // Put the new comment into the comments map
                                        comments.put("comment" + (comments.size() + 1), newComment);

                                        // Put the data back into the database
                                        database.collection("posts").document(RecyclerViewAdapterSocial.selectedPostID)
                                                .update("comments", comments);

                                        // Reset the edit box
                                        commentEdit.setText("");
                                        commentEdit.setHint("Type your comment here");
                                        getComments();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
    }
}
