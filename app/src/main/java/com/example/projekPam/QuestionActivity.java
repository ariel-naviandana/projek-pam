package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityQuestionBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuestionBinding binding;
    private QuestionAdapter questionAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        String quizId = getIntent().getStringExtra("QUIZ_ID");
        String quizTitle = getIntent().getStringExtra("QUIZ_TITLE");

        binding.title.setText(quizTitle);

        binding.btnBack.setOnClickListener(this);
        binding.btnAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionFormActivity.class);
            intent.putExtra("QUIZ_ID", quizId);
            startActivity(intent);
        });

        ArrayList<Question> questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList, db, quizId);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(questionAdapter);

        db.collection("kuis").document(quizId).collection("soal").orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Gagal memuat soal", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    questionList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot document : value) {
                            String id = document.getId();
                            String question = document.getString("pertanyaan");
                            String type = document.getString("tipe");
                            String answer = document.getString("jawaban");
                            List<String> options = (List<String>) document.get("pilihan");
                            questionList.add(new Question(id, question, type, answer, options));
                        }
                        questionAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnBack)
            finish();
        else if (id == R.id.btnAddQuestion) {
            Intent intent = new Intent(this, QuestionFormActivity.class);
            startActivity(intent);
        }
    }
}