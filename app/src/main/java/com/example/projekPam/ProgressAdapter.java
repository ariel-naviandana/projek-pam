package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {
    private List<ProgressItem> progressItemList;

    public ProgressAdapter(List<ProgressItem> progressItemList) {
        this.progressItemList = progressItemList;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        ProgressItem progressItem = progressItemList.get(position);
        holder.tvLabel.setText(progressItem.getLabel());
        holder.ivIcon.setImageResource(progressItem.getIconResId());
        holder.progressBar.setProgress(progressItem.getProgress());
        holder.tvPercent.setText(progressItem.getProgress() + "%");
    }

    @Override
    public int getItemCount() {
        return progressItemList.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView tvLabel;
        ImageView ivIcon;
        ProgressBar progressBar;
        TextView tvPercent;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            progressBar = itemView.findViewById(R.id.progressBar);
            tvPercent = itemView.findViewById(R.id.tvPercent);
        }
    }
}