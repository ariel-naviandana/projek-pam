package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekPam.databinding.ItemChallengeBinding;
import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Challenge> challengeList;

    // Constructor
    public ChallengeAdapter(List<Challenge> challengeList) {
        this.challengeList = challengeList;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChallengeBinding binding = ItemChallengeBinding.inflate(inflater, parent, false);
        return new ChallengeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);
        holder.binding.challengeTitle.setText(challenge.getTitle());
        holder.binding.challengePeriod.setText(challenge.getPeriod());
        holder.binding.challengeImage.setImageResource(challenge.getImage());
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    public class ChallengeViewHolder extends RecyclerView.ViewHolder {
        ItemChallengeBinding binding;

        public ChallengeViewHolder(@NonNull ItemChallengeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
