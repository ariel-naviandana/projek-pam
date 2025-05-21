package com.example.projekPam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projekPam.databinding.ItemMateriBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private final Context context;
    private final List<Materi> materiList;
    private String userRole = "user"; // Default role
    private OnItemClickListener listener;

    // Interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(Materi materi);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    // Constructor
    public MateriAdapter(Context context, List<Materi> materiList, String userRole) {
        this.context = context;
        this.materiList = materiList;
        this.userRole = userRole;
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

        // Handle tvLinkMateri (using deskripsi as detail)
        holder.binding.tvLinkMateri.setText(materi.getDeskripsi() != null ? materi.getDeskripsi() : "Lihat Detail");

        // Handle download progress (placeholder)
        holder.binding.tvPersenMateri.setText("0%");
        holder.binding.imgDownloadBar.setVisibility(View.GONE);

        // Role-based visibility for delete button
        holder.binding.btnDeleteMateri.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);

        // Klik tombol download (placeholder)
        holder.binding.btnDownloadMateri.setOnClickListener(v -> {
            holder.binding.imgDownloadBar.setVisibility(View.VISIBLE);
            holder.binding.tvPersenMateri.setText("Downloading...");
        });

        // Klik tombol delete
        holder.binding.btnDeleteMateri.setOnClickListener(v -> {
            if (userRole.equals("admin")) {
                FirebaseFirestore.getInstance()
                        .collection("materi")
                        .document(materi.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Materi dihapus", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                        });
            }
        });

        // Item click for edit or view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(materi);
            }
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