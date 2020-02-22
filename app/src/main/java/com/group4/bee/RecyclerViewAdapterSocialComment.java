package com.group4.bee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterSocialComment extends RecyclerView.Adapter<RecyclerViewAdapterSocialComment.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> user;
    private ArrayList<String> commentContent;
    private int numOfItems;

    public RecyclerViewAdapterSocialComment(ArrayList<String> userName, ArrayList<String> commentContent, Context context) {
        this.user = userName;
        this.mInflater = LayoutInflater.from(context);
        this.commentContent = commentContent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.comment_holder, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView commentorName = holder.commentorName;
        final TextView commentContent = holder.commentContent;

        commentorName.setText(this.user.get(position));

        commentContent.setText(this.commentContent.get(position));
    }

    public int getItemCount() {
        numOfItems = user.size();
        return numOfItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView commentorName;
        TextView commentContent;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            commentorName = itemView.findViewById(R.id.commentorName);
            commentContent = itemView.findViewById(R.id.commentContent);
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
                commentContent.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }
}