package com.example.projekPam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Added for ImageView
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Added for Glide
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private ArrayList<Question> questionList;
    private FirebaseFirestore db;
    private String quizId;

    public QuestionAdapter(ArrayList<Question> questionList, FirebaseFirestore db, String quizId) {
        this.questionList = questionList;
        this.db = db;
        this.quizId = quizId;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvQuestion.setText(question.getQuestion());
        holder.tvQuestionType.setText("Tipe: " + question.getType());
        holder.tvCorrectAnswer.setText("Jawaban Benar: " + question.getAnswer());

        // Load image with Glide
        if (question.getImageUrl() != null && !question.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(question.getImageUrl())
                    .placeholder(R.drawable.avatar) // Default image while loading
                    .error(R.drawable.avatar) // Default image if URL fails
                    .into(holder.ivQuestionImage);
        } else {
            holder.ivQuestionImage.setImageResource(R.drawable.avatar); // Default image if null
        }

        holder.tvQuestion.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Pertanyaan: " + question.getQuestion(), Toast.LENGTH_SHORT).show();
        });

        holder.btnEditQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), QuestionFormActivity.class);
            intent.putExtra("QUIZ_ID", quizId);
            intent.putExtra("QUESTION_ID", question.getId());
            v.getContext().startActivity(intent);
        });

        holder.btnDeleteQuestion.setOnClickListener(v -> {
            showDeleteConfirmationDialog(v.getContext(), question, position);
        });
    }

    private void showDeleteConfirmationDialog(Context context, Question question, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus soal \"" + question.getQuestion() + "\"?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    db.collection("kuis").document(quizId).collection("soal").document(question.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Soal dihapus", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(context, "Gagal menghapus soal", Toast.LENGTH_SHORT).show()
                            );
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvQuestionType, tvCorrectAnswer;
        ImageView ivQuestionImage; // Added for ImageView
        View btnEditQuestion, btnDeleteQuestion;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvQuestionType = itemView.findViewById(R.id.tvQuestionType);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage); // Initialize ImageView
            btnEditQuestion = itemView.findViewById(R.id.btnEditQuestion);
            btnDeleteQuestion = itemView.findViewById(R.id.btnDeleteQuestion);
        }
    }
}