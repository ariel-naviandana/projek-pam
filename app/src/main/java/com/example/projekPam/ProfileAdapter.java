package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<DaftarTemanEntry> daftarTemanList;

    public ProfileAdapter(List<DaftarTemanEntry> daftarTemanList) {
        this.daftarTemanList = daftarTemanList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daftar_teman, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        DaftarTemanEntry entry = daftarTemanList.get(position);

        holder.tvName.setText(entry.getNama());
        holder.tvUsername.setText(entry.getUsername());

    }

    @Override
    public int getItemCount() {
        return daftarTemanList.size();
    }

    public void updateData(List<DaftarTemanEntry> newList) {
        daftarTemanList = newList;
        notifyDataSetChanged();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername;
        ImageView ivProfile;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }
    }
}
