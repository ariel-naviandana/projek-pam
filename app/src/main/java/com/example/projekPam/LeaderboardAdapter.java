package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private ArrayList<LeaderboardEntry> leaderboardList;

    public LeaderboardAdapter(ArrayList<LeaderboardEntry> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardEntry entry = leaderboardList.get(position);

        holder.tvRank.setText(String.valueOf(entry.getRanking()));
        holder.tvName.setText(entry.getNama());
        holder.tvUsername.setText(entry.getUsername());
        holder.tvScore.setText(String.valueOf(entry.getSkor()));
        holder.ivProfile.setImageResource(R.drawable.avatar);

        holder.tvRank.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public void updateData(ArrayList<LeaderboardEntry> newList) {
        leaderboardList = newList;
        notifyDataSetChanged();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvUsername, tvScore;
        ImageView ivProfile;

        public LeaderboardViewHolder(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvName = itemView.findViewById(R.id.tvName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvScore = itemView.findViewById(R.id.tvScore);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
}
