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

        db = FirebaseFirestore.getInstance();

        binding.home.setOnClickListener(this);
        binding.quiz.setOnClickListener(this);
        binding.challenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
        binding.materi.setOnClickListener(this);
        binding.btnAddQuiz.setOnClickListener(this);

        quizAdapter = new QuizAdapter(new ArrayList<>(), db);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(quizAdapter);

        setupFirestoreListener();
    }

    private void setupFirestoreListener() {
        db.collection("kuis").addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("QuizActivity", "Listen failed.", error);
                return;
            }

            if (value != null) {
                ArrayList<Quiz> quizList = new ArrayList<>();
                for (QueryDocumentSnapshot quizDocument : value) {
                    String quizId = quizDocument.getId();
                    String title = quizDocument.getString("judul");
                    String idMateri = quizDocument.getString("id_materi");
                    String difficulty = quizDocument.getString("kesulitan");
                    String deskripsi = quizDocument.getString("deskripsi");

                    db.collection("materi").document(idMateri).get()
                            .addOnSuccessListener(materiDocument -> {
                                String materiTitle = materiDocument.getString("judul");

                                db.collection("kuis").document(quizId).collection("soal")
                                        .get()
                                        .addOnCompleteListener(soalTask -> {
                                            if (soalTask.isSuccessful()) {
                                                int questionCount = soalTask.getResult().size();

                                                quizList.add(new Quiz(quizId, title, materiTitle, difficulty, deskripsi, questionCount));
                                                quizAdapter.updateQuizList(quizList);
                                            } else
                                                Log.w("QuizActivity", "Error getting soal.", soalTask.getException());
                                        });
                            })
                            .addOnFailureListener(e -> Log.w("QuizActivity", "Error fetching materi title.", e));
                }
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
        else if (id == R.id.materi)
            Toast.makeText(this, "Halaman Materi belum ada", Toast.LENGTH_SHORT).show();
        else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnAddQuiz) {
            Intent intent = new Intent(this, QuizFormActivity.class);
            startActivity(intent);
        }
    }
}