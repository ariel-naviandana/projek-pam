package com.example.projekPam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private ArrayList<Quiz> quizList;
    private FirebaseFirestore db;

    public QuizAdapter(ArrayList<Quiz> quizList, FirebaseFirestore db) {
        this.quizList = quizList;
        this.db = db;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvQuizTitle.setText(quiz.getTitle());
        holder.tvQuizCategory.setText("Kategori: " + quiz.getCategory());
        holder.tvQuizDifficulty.setText("Kesulitan: " + quiz.getDifficulty());
        holder.tvQuizQuestionCount.setText("Jumlah Soal: " + quiz.getQuestionCount());

        holder.btnSoal.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "List soal untuk " + quiz.getTitle(), Toast.LENGTH_SHORT).show();
        });

        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Edit " + quiz.getTitle(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v.getContext(), quiz, position);
        });
    }

    private void showDeleteConfirmationDialog(Context context, Quiz quiz, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus kuis \"" + quiz.getTitle() + "\"?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    // Delete from Firestore using id
                    db.collection("kuis").document(quiz.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                quizList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, quiz.getTitle() + " dihapus", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Gagal menghapus kuis", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void updateQuizList(ArrayList<Quiz> newQuizList) {
        this.quizList = newQuizList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizTitle, tvQuizCategory, tvQuizDifficulty, tvQuizQuestionCount;
        View btnEdit, btnDelete, btnSoal;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvQuizCategory = itemView.findViewById(R.id.tvQuizCategory);
            tvQuizDifficulty = itemView.findViewById(R.id.tvQuizDifficulty);
            tvQuizQuestionCount = itemView.findViewById(R.id.tvQuizQuestionCount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnSoal = itemView.findViewById(R.id.btnQuestionList);
        }
    }
}