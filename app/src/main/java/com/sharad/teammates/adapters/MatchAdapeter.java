package com.sharad.teammates.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sharad.teammates.R;
import com.sharad.teammates.activities.ChatActivity;
import com.sharad.teammates.models.Match;

import java.util.List;

public class MatchAdapeter extends RecyclerView.Adapter<MatchAdapeter.ViewHolder> {

    private final Context mContext;
    private final List<Match> matchesList;

    public MatchAdapeter(Context mContext, List<Match> matchesList) {
        this.mContext = mContext;
        this.matchesList = matchesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.matches_for_recycler_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.matchId.setText(matchesList.get(i).getMatchStudentId());
        viewHolder.matchName.setText(matchesList.get(i).getStudentName());

        if(!matchesList.get(i).getProfileImage().equalsIgnoreCase(mContext.getString(R.string.default_image))){
            Glide.with(mContext).asBitmap().load(matchesList.get(i).getProfileImage()).into(viewHolder.imageForMatch);
        }
        viewHolder.matchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("matchID",viewHolder.matchId.getText().toString());
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView matchId, matchName;
        ImageView imageForMatch;
        LinearLayout matchLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            matchId = itemView.findViewById(R.id.matchId);
            matchName = itemView.findViewById(R.id.matchName);
            imageForMatch = itemView.findViewById(R.id.imageForMatch);
            matchLinearLayout = itemView.findViewById(R.id.matchLinearLayout);
        }
    }
}
