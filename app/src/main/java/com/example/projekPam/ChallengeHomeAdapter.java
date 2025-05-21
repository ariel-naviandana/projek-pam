package com.example.projekPam;

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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChallengeHomeAdapter extends RecyclerView.Adapter<ChallengeHomeAdapter.ChallengeViewHolder> {

    private final Context context;
    private final List<Challenge> challengeList;

    public ChallengeHomeAdapter(Context context, List<Challenge> challengeList) {
        this.context = context;
        this.challengeList = challengeList;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_challenge_home, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);
        holder.judulChallenge.setText(challenge.getJudul());

        // Load image using Glide
        if (challenge.getImage() != null && !challenge.getImage().isEmpty()) {
            Glide.with(context)
                    .load(challenge.getImage())
                    .placeholder(R.drawable.poster_challenge)
                    .error(R.drawable.poster_challenge)
                    .into(holder.challengeImg);
        } else {
            holder.challengeImg.setImageResource(R.drawable.poster_challenge);
        }

        // Format period
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String period = sdf.format(challenge.getDate_start().toDate()) + " - " +
                sdf.format(challenge.getDate_end().toDate());
        holder.periodeChallenge.setText(period);
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        ImageView challengeImg;
        TextView judulChallenge, periodeChallenge;

        ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            challengeImg = itemView.findViewById(R.id.challengeImg);
            judulChallenge = itemView.findViewById(R.id.judulChallenge);
            periodeChallenge = itemView.findViewById(R.id.periodeChallenge);
        }
    }
}