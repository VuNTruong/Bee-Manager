package com.group4.bee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterCompetitorResult extends RecyclerView.Adapter<RecyclerViewAdapterCompetitorResult.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> startTime;
    private ArrayList<String> endTime;
    private ArrayList<String> duration;
    private ArrayList<String> score;
    private ArrayList<String> testNumber;

    public RecyclerViewAdapterCompetitorResult(ArrayList<String> startTime, ArrayList<String> endTime, ArrayList<String> duration
            , ArrayList<String> score, ArrayList<String> testNumber, Context context) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.score = score;
        this.testNumber = testNumber;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.competitor_result, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView startTime = holder.startTime;
        TextView endTime = holder.endTime;
        TextView duration = holder.duration;
        TextView score = holder.score;
        TextView testNumber = holder.testNumber;

        startTime.setText(this.startTime.get(position));
        endTime.setText(this.endTime.get(position));
        duration.setText(this.duration.get(position));
        score.setText(this.score.get(position));
        testNumber.setText(this.testNumber.get(position));
    }

    public int getItemCount() {
        return duration.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView startTime;
        TextView endTime;
        TextView duration;
        TextView score;
        TextView testNumber;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            testNumber = itemView.findViewById(R.id.testNumberCompetitor);
            startTime = itemView.findViewById(R.id.startTimeCompetitor);
            endTime = itemView.findViewById(R.id.endTimeCompetitor);
            duration = itemView.findViewById(R.id.durationCompetitor);
            score = itemView.findViewById(R.id.scoreCompetitor);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }
}
