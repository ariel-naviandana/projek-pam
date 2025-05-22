package com.example.projekPam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private List<LeaderboardEntry> leaderboardList;
    private Context context;

    public LeaderboardAdapter(Context context, List<LeaderboardEntry> leaderboardList) {
        this.leaderboardList = leaderboardList;
        this.context = context;
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

        if (entry.getImage() != null && !entry.getImage().isEmpty()) {
            Glide.with(context)
                    .load(entry.getImage())
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(R.drawable.avatar);
        }

        holder.tvRank.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public void updateData(List<LeaderboardEntry> newList) {
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
