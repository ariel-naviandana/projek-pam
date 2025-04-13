package com.example.projekPam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private ArrayList<Question> questionList;

    public QuestionAdapter(ArrayList<Question> questionList) {
        this.questionList = questionList;
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
            Toast.makeText(v.getContext(), "Edit soal: " + question.getQuestion(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDeleteQuestion.setOnClickListener(v -> {
            questionList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(v.getContext(), "Soal dihapus.", Toast.LENGTH_SHORT).show();
        });
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