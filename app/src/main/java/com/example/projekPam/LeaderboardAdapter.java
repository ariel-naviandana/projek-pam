package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekPam.databinding.ItemLeaderboardBinding;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private List<LeaderboardEntry> leaderboardList;

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLeaderboardBinding binding = ItemLeaderboardBinding.inflate(inflater, parent, false);
        return new LeaderboardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        holder.bind(leaderboardList.get(position));
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
        private final ItemLeaderboardBinding binding;

        public LeaderboardViewHolder(ItemLeaderboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LeaderboardEntry entry) {
            binding.setEntry(entry);
            binding.executePendingBindings();
        }
    }
}
