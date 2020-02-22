package com.group4.bee;

import android.content.Context;
import android.content.Intent;
import android.telephony.SubscriptionPlan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterLeaderboard extends RecyclerView.Adapter<RecyclerViewAdapterLeaderboard.ViewHolder> implements Filterable {
    private LayoutInflater mInflater;
    private ArrayList<UserClass> user;
    private int numOfItems;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<UserClass> userFull;

    public static String selectedUserEmail;

    public RecyclerViewAdapterLeaderboard(ArrayList<UserClass> userObjectArray, Context context) {
        this.user = userObjectArray;
        this.mInflater = LayoutInflater.from(context);

        userFull = new ArrayList<>(user);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.competitor_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView quiznumber = holder.name;
        final ImageView userAvatar = holder.avatarCompetitor;

        quiznumber.setText(this.user.get(position).getName());

        Glide.with(mInflater.getContext())
                .load(this.user.get(position).getAvatarURL())
                .into(userAvatar);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUserEmail = user.get(position).getEmail();
                Intent intent = new Intent(v.getContext(), UserProfile.class);
                v.getContext().startActivity(intent);
            }
        });

        // Check if the user has been followed or not
        checkFollowFunction(user.get(position).getEmail(), holder.follow);
    }

    public int getItemCount() {
        numOfItems = user.size();
        return numOfItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        CircleImageView avatarCompetitor;
        Button follow;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.competitorName);
            avatarCompetitor = itemView.findViewById(R.id.avatarCompetitor);
            follow = itemView.findViewById(R.id.follow);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public Filter getFilter() {
        return leaderBoardFilter;
    }

    private Filter leaderBoardFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<UserClass> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(userFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (UserClass item : userFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            user.clear();
            user.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    private void setDataFollowing (final String userEmail) {
        String currentUserEmail;

        if (Welcome.user != null) {
            currentUserEmail = Welcome.user.getEmail();
        } else {
            currentUserEmail = Splash.user.getEmail();
        }

        final DocumentReference documentReference = db.collection("user")
                .document(currentUserEmail);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;
                        HashMap<String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("following");

                        numOfItems = map.size();

                        DocumentReference documentReference;

                        if (Welcome.user != null) {
                            documentReference = db.collection("user").document(Welcome.user.getEmail());
                        } else {
                            documentReference = db.collection("user").document(Splash.user.getEmail());
                        }

                        Map<String, Object> data = new HashMap<>();
                        data.put("following.user" + (numOfItems + 1), userEmail);

                        documentReference
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Message", "Error adding document", e);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setDataFollower (final String userEmail, final String currentUserEmail) {

        final DocumentReference documentReference = db.collection("user")
                .document(userEmail);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;
                        HashMap<String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("follower");

                        numOfItems = map.size();

                        DocumentReference documentReference;

                        documentReference = db.collection("user").document(userEmail);

                        Map<String, Object> data = new HashMap<>();
                        data.put("follower.user" + (numOfItems + 1), currentUserEmail);

                        documentReference
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Message", "Error adding document", e);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void followFunction (final String userEmail, Button followButton) {
        final String currentUserEmail;

        if (Welcome.user != null) {
            currentUserEmail = Welcome.user.getEmail();
        } else {
            currentUserEmail = Splash.user.getEmail();
        }

        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = db.collection("user")
                .document(currentUserEmail);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;

                        Map<String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("following");

                        numOfItems = map.size();

                        Log.d("Message", "" + numOfItems);

                        // ArrayList to contain list of currently following users
                        ArrayList<String> currentlyFollowingUser = new ArrayList();

                        for (int i = 0; i < numOfItems; i++) {
                            currentlyFollowingUser.add(map.get("user" + (i + 1)).toString());
                        }

                        if (currentlyFollowingUser.contains(userEmail)) {
                            System.out.println("Already followed");
                        } else {
                            setDataFollowing(userEmail);
                            setDataFollower(userEmail, currentUserEmail);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Set the button to unfollow
        followButton.setText("Unfollow");
    }

    private void checkFollowFunction (final String userEmail, final Button followButton) {
        final String currentUserEmail;

        if (Welcome.user != null) {
            currentUserEmail = Welcome.user.getEmail();
        } else {
            currentUserEmail = Splash.user.getEmail();
        }

        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = db.collection("user")
                .document(currentUserEmail);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;

                        Map<String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("following");

                        numOfItems = map.size();

                        Log.d("Message", "" + numOfItems);

                        // ArrayList to contain list of currently following users
                        ArrayList<String> currentlyFollowingUser = new ArrayList();

                        for (int i = 0; i < numOfItems; i++) {
                            currentlyFollowingUser.add(map.get("user" + (i + 1)).toString());
                        }

                        if (currentlyFollowingUser.contains(userEmail)) {
                            followButton.setText("Unfollow");
                            followButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    unfollowFunction(userEmail, followButton);
                                }
                            });
                        } else {
                            followButton.setText("Follow");
                            followButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    followFunction(userEmail, followButton);
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void unfollowFunction (final String userEmail, final Button unfollowButton) {
        final String currentUserEmail;

        if (Welcome.user != null) {
            currentUserEmail = Welcome.user.getEmail();
        } else {
            currentUserEmail = Splash.user.getEmail();
        }

        // DocumentReference which is used to reference to the collection which contains users
        final DocumentReference documentReference = db.collection("user")
                .document(currentUserEmail);

        // Update the following
        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;

                        Map<String, Object> map;
                        ArrayList<String> currentFollowingUserEmail = new ArrayList<>();

                        map = (HashMap<String, Object>) documentSnapshot.get("following");

                        numOfItems = map.size();

                        for (int i = 0; i < numOfItems; i++) {
                            currentFollowingUserEmail.add(map.get("user" + (i+1)).toString());
                        }

                        // Remove the targeted user away from the list of currently following user
                        currentFollowingUserEmail.remove(userEmail);

                        // Update the list
                        updateFollowingList(currentUserEmail, currentFollowingUserEmail);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Update the follower
        // DocumentReference which is used to reference to the collection which contains user
        final DocumentReference documentReference1 = db.collection("user")
                .document(userEmail);

        // Use the DocumentReference to get the information
        documentReference1.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;

                        Map<String, Object> map;
                        ArrayList<String> currentFollowerUserEmail = new ArrayList<>();

                        map = (HashMap<String, Object>) documentSnapshot.get("follower");

                        numOfItems = map.size();

                        for (int i = 0; i < numOfItems; i++) {
                            currentFollowerUserEmail.add(map.get("user" + (i+1)).toString());
                        }

                        // Remove the targeted user away from the list of currently following user
                        currentFollowerUserEmail.remove(currentUserEmail);

                        // Update the list
                        updateFollowerList(userEmail, currentFollowerUserEmail);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Set the button to follow
        unfollowButton.setText("Follow");
    }

    private void updateFollowingList (final String userEmail, final ArrayList<String> newUserEmailList) {
        final DocumentReference documentReference = db.collection("user")
                .document(userEmail);

        HashMap<String, Object> newList = new HashMap<>();

        for (int i = 0; i < newUserEmailList.size(); i++) {
            newList.put("user" + (i+1), newUserEmailList.get(i));
        }

        documentReference.update("following", newList);
    }

    private void updateFollowerList (final String removedEmail, final ArrayList<String> newUserEmailList) {
        final DocumentReference documentReference = db.collection("user")
                .document(removedEmail);

        HashMap<String, Object> newList = new HashMap<>();

        for (int i = 0; i < newUserEmailList.size(); i++) {
            newList.put("user" + (i+1), newUserEmailList.get(i));
        }

        documentReference.update("follower", newList);
    }
}