package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekPam.databinding.ItemMateriBinding;
import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {

    private List<Materi> materiList;

    // Constructor
    public MateriAdapter(List<Materi> materiList) {
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
        holder.binding.tvNamaMateri.setText(materi.getNamaMateri());
        holder.binding.imgMateri.setImageResource(materi.getImgMateriResId());

        // Klik tombol download/delete
        holder.binding.btnDownloadMateri.setOnClickListener(v -> {
            // Tambahkan aksi download
        });

        holder.binding.btnDeleteMateri.setOnClickListener(v -> {
            // Tambahkan aksi delete
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
