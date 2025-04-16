package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityQuizBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuizBinding binding;
    private QuizAdapter quizAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        binding.home.setOnClickListener(this);
        binding.quiz.setOnClickListener(this);
        binding.challenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
        binding.btnAddQuiz.setOnClickListener(this);

        ArrayList<Quiz> quizList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(quizAdapter);

        // Load quizzes from Firestore
        loadQuizzesFromFirestore();
    }

    private void loadQuizzesFromFirestore() {
        db.collection("kuis")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Quiz> quizList = new ArrayList<>();
                        for (QueryDocumentSnapshot quizDocument : task.getResult()) {
                            String quizId = quizDocument.getId();
                            String title = quizDocument.getString("judul");
                            String idMateri = quizDocument.getString("id_materi");
                            String difficulty = quizDocument.getString("kesulitan");

                            // Fetch materi title and count soal
                            db.collection("materi").document(idMateri).get()
                                    .addOnSuccessListener(materiDocument -> {
                                        String materiTitle = materiDocument.getString("judul");

                                        // Count soal in sub-collection
                                        db.collection("kuis").document(quizId).collection("soal")
                                                .get()
                                                .addOnCompleteListener(soalTask -> {
                                                    if (soalTask.isSuccessful()) {
                                                        int questionCount = soalTask.getResult().size();

                                                        // Add quiz to list
                                                        quizList.add(new Quiz(title, materiTitle, difficulty, questionCount));
                                                        quizAdapter.updateQuizList(quizList);
                                                    } else {
                                                        Log.w("QuizActivity", "Error getting soal count.", soalTask.getException());
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> Log.w("QuizActivity", "Error fetching materi title.", e));
                        }
                    } else {
                        Log.w("QuizActivity", "Error getting quizzes.", task.getException());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.quiz)
            Toast.makeText(this, "Anda sudah di halaman kuis.", Toast.LENGTH_SHORT).show();
        else if (id == R.id.challenge)
            Toast.makeText(this, "Halaman Challenge belum ada", Toast.LENGTH_SHORT).show();
        else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnAddQuiz) {
            Intent intent = new Intent(this, QuizFormActivity.class);
            startActivity(intent);
        }
    }
}