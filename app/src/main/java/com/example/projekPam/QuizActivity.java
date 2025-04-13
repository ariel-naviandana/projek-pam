package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityQuizBinding;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuizBinding binding;
    private QuizAdapter quizAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(this);
        binding.quiz.setOnClickListener(this);
        binding.challenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
        binding.btnAddQuiz.setOnClickListener(this);

        ArrayList<Quiz> quizList = new ArrayList<>();
        quizList.add(new Quiz("Kuis 1", "Lingkungan", "Mudah", 10));
        quizList.add(new Quiz("Kuis 2", "Energi", "Sedang", 7));
        quizList.add(new Quiz("Kuis 3", "Daur Ulang", "Sulit", 15));

        quizAdapter = new QuizAdapter(quizList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(quizAdapter);
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