package com.group4.bee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterFollower extends RecyclerView.Adapter<RecyclerViewAdapterFollower.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<UserClass> user;
    private int numOfItems;

    public static String selectedUserEmail;

    public RecyclerViewAdapterFollower(ArrayList<UserClass> userObjectArray, Context context) {
        this.user = userObjectArray;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.follower_show, parent, false);
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
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.followerName);
            avatarCompetitor = itemView.findViewById(R.id.avatarFollower);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }
}