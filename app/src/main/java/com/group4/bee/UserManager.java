package com.group4.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UserManager extends AppCompatActivity  {

    FirebaseFirestore database;

    RecyclerViewAdapterLeaderboard adapter;

    public ArrayList<String> email = new ArrayList<>();
    public ArrayList<String> name = new ArrayList<>();
    public ArrayList<String> avatarURL = new ArrayList<>();

    // User objects
    public ArrayList<UserClass> userObjectArray = new ArrayList<>();

    EditText searchBox;

    @Override
    public void onBackPressed () {
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        database = FirebaseFirestore.getInstance();

        searchBox = findViewById(R.id.searchBoxEdit);

        getDocuments();
    }

    private void getDocuments () {
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        db.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                avatarURL.add(document.getData().get("avatarURL").toString());

                                map = (HashMap<String, Object>) document.getData().get("user info");

                                email.add(document.getId());
                                name.add(document.getString("firstname") + " " + document.getString("middlename").toString() + " " + document.getString("lastname"));

                                // Add user to the user object array
                                userObjectArray.add(new UserClass(document.getString("firstname") + " " + document.getString("middlename") + " " + document.getString("lastname"), document.getId(),
                                        document.getString("avatarURL")));

                                // This one is used to search for user
                                searchBox.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                        adapter.getFilter().filter(charSequence);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                    }
                                });

                                final RecyclerView recyclerView = findViewById(R.id.competitors);
                                recyclerView.setLayoutManager(new LinearLayoutManager(UserManager.this));
                                adapter = new RecyclerViewAdapterLeaderboard(userObjectArray, UserManager.this);

                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d("Message", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
