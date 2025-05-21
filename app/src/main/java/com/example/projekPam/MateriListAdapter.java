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
import java.util.List;

public class MateriListAdapter extends RecyclerView.Adapter<MateriListAdapter.MateriViewHolder> {

    private final Context context;
    private final List<Materi> materiList;
    private OnItemClickListener onItemClickListener;

    public MateriListAdapter(Context context, List<Materi> materiList) {
        this.context = context;
        this.materiList = materiList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_materi, parent, false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        Materi materi = materiList.get(position);
        holder.tvJudulMateri.setText(materi.getJudul());

        // Load image using Glide
        if (materi.getImage() != null && !materi.getImage().isEmpty()) {
            Glide.with(context)
                    .load(materi.getImage())
                    .placeholder(R.drawable.book_icon)
                    .error(R.drawable.book_icon)
                    .into(holder.imgMateri);
        } else {
            holder.imgMateri.setImageResource(R.drawable.book_icon);
        }
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    static class MateriViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMateri;
        TextView tvJudulMateri;

        MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMateri = itemView.findViewById(R.id.imgMateri);
            tvJudulMateri = itemView.findViewById(R.id.tvJudulMateri);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Materi materi);
    }
}