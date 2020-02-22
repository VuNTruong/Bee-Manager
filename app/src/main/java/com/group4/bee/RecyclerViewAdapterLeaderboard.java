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
}