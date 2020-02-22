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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterSocial extends RecyclerView.Adapter<RecyclerViewAdapterSocial.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<UserClass> user;
    private ArrayList<String> postContent;
    private ArrayList<String> postID;
    private int numOfItems;

    public static String selectedPostID;

    public RecyclerViewAdapterSocial(ArrayList<UserClass> userObjectArray, ArrayList<String> postContent, ArrayList<String> postID, Context context) {
        this.user = userObjectArray;
        this.mInflater = LayoutInflater.from(context);
        this.postContent = postContent;
        this.postID = postID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_holder, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView userName = holder.userName;
        final ImageView userAvatar = holder.userAvatar;
        final TextView postContent = holder.postContent;
        final ImageView commentButton = holder.commentButton;

        userName.setText(this.user.get(position).getName());

        Glide.with(mInflater.getContext())
                .load(this.user.get(position).getAvatarURL())
                .into(userAvatar);

        postContent.setText(this.postContent.get(position));

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mInflater.getContext(), CommentActivity.class);
                mInflater.getContext().startActivity(intent);

                selectedPostID = postID.get(position);
            }
        });
    }

    public int getItemCount() {
        numOfItems = user.size();
        return numOfItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        CircleImageView userAvatar;
        TextView postContent;
        ImageView commentButton;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.nameForum);
            userAvatar = itemView.findViewById(R.id.avatarForum);
            commentButton = itemView.findViewById(R.id.commentButton);
            postContent = itemView.findViewById(R.id.postContent);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void clear() {
        int size = user.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                user.remove(0);
                postContent.remove(0);
                postID.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}