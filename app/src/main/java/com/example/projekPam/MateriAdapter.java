package com.example.projekPam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projekPam.databinding.ItemMateriBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private final Context context;
    private final List<Materi> materiList;
    private OnItemClickListener listener;

    // Interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Materi materi);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public MateriAdapter(Context context, List<Materi> materiList) {
        this.context = context;
        this.materiList = materiList;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMateriBinding binding = ItemMateriBinding.inflate(inflater, parent, false);
        return new MateriViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        Materi materi = materiList.get(position);
        holder.binding.tvNamaMateri.setText(materi.getJudul());

        // Load image using Glide
        if (materi.getImage() != null && !materi.getImage().isEmpty()) {
            Glide.with(context)
                    .load(materi.getImage())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.binding.imgMateri);
        } else {
            holder.binding.imgMateri.setImageResource(R.drawable.book_icon);
        }

        // Handle tvLinkMateri (placeholder, update if Firestore has a link field)
        holder.binding.tvLinkMateri.setText("Lihat Detail");

        // Handle download progress (placeholder, update if Firestore tracks progress)
        holder.binding.tvPersenMateri.setText("0%");
        holder.binding.imgDownloadBar.setVisibility(View.GONE); // Hide until download is implemented

        // Klik tombol download
        holder.binding.btnDownloadMateri.setOnClickListener(v -> {
            // Placeholder for download action
            // Example: Download materi.getImage()
            // For now, simulate progress
            holder.binding.imgDownloadBar.setVisibility(View.VISIBLE);
            holder.binding.tvPersenMateri.setText("Downloading...");
        });

        // Klik tombol delete
        holder.binding.btnDeleteMateri.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("materi")
                    .document(materi.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        materiList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, materiList.size());
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                    });
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        ItemMateriBinding binding;

        public MateriViewHolder(@NonNull ItemMateriBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}