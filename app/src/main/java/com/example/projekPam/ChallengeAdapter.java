package com.example.projekPam;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projekPam.databinding.ItemChallengeBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {
    private List<Challenge> challengeList;
    private final FirebaseFirestore db;
    private final Context context;
    private String userRole = "user"; // Default role

    public ChallengeAdapter(List<Challenge> challengeList, Context context, FirebaseFirestore db, String userRole) {
        this.challengeList = challengeList;
        this.context = context;
        this.db = db;
        this.userRole = userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChallengeBinding binding = ItemChallengeBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ChallengeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challengeList.get(position);

        String dateRange = formatDateRange(challenge.getDate_start(), challenge.getDate_end());

        holder.binding.challengeTitle.setText(challenge.getJudul());
        holder.binding.challengeDate.setText(dateRange);

        if (challenge.getJudul() != null && challenge.getJudul().equalsIgnoreCase("Agustus Challenge Mengurangi Sampah Plastik")) {
            holder.binding.challengeImage.setImageResource(R.drawable.poster_challenge);
        } else if (challenge.getJudul() != null && challenge.getJudul().equalsIgnoreCase("September Challenge Menanam Pohon")) {
            holder.binding.challengeImage.setImageResource(R.drawable.poster_challenge2);
        } else {
            holder.binding.challengeImage.setImageResource(R.drawable.challenge_image);
        }

        // Show/hide edit and delete buttons based on user role
        holder.binding.btnEdit.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
        holder.binding.btnHapus.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);

        holder.binding.btnEdit.setOnClickListener(v -> {
            if (userRole.equals("admin")) {
                Intent intent = new Intent(context, ChallengeFormActivity.class);
                intent.putExtra("CHALLENGE_ID", challenge.getId());
                intent.putExtra("JUDUL", challenge.getJudul());
                intent.putExtra("DATE_START", challenge.getDate_start().toDate().getTime());
                intent.putExtra("DATE_END", challenge.getDate_end().toDate().getTime());
                context.startActivity(intent);
            }
        });

        holder.binding.btnHapus.setOnClickListener(v -> {
            if (userRole.equals("admin")) {
                showDeleteConfirmationDialog(challenge, position);
            }
        });
    }

    private String formatDateRange(Timestamp start, Timestamp end) {
        if (start == null || end == null) {
            return "Tanggal tidak tersedia";
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            return sdf.format(start.toDate()) + " - " + sdf.format(end.toDate());
        } catch (Exception e) {
            Log.e("DateFormatError", "Error formatting date", e);
            return "Format tanggal salah";
        }
    }

    private void showDeleteConfirmationDialog(Challenge challenge, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus challenge " + challenge.getJudul() + "?")
                .setPositiveButton("Hapus", (dialog, which) -> deleteChallenge(challenge, position))
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteChallenge(Challenge challenge, int position) {
        String challengeIdToDelete = challenge.getId();

        challengeList.remove(position);
        notifyItemRemoved(position);

        Toast.makeText(context, "Menghapus challenge..", Toast.LENGTH_SHORT).show();

        db.collection("challenge").document(challengeIdToDelete)
                .delete()
                .addOnFailureListener(e -> {
                    challengeList.add(position, challenge);
                    notifyItemInserted(position);
                    Toast.makeText(context, "Gagal menghapus: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        final ItemChallengeBinding binding;

        public ChallengeViewHolder(ItemChallengeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}