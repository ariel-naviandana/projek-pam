package com.example.projekPam;

import android.content.Context;
import android.content.Intent;
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
                                questionList.remove(position);
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
        View btnEditQuestion, btnDeleteQuestion;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvQuestionType = itemView.findViewById(R.id.tvQuestionType);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            btnEditQuestion = itemView.findViewById(R.id.btnEditQuestion);
            btnDeleteQuestion = itemView.findViewById(R.id.btnDeleteQuestion);
        }
    }
}