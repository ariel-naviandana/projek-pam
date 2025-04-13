package com.example.projekPam;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityQuizFormBinding;

public class QuizFormActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuizFormBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDifficultySpinner();

        binding.btnBack.setOnClickListener(this);
        binding.btnSaveQuiz.setOnClickListener(this);
    }

    private void setupDifficultySpinner() {
        String[] difficultyLevels = {"Mudah", "Sedang", "Sulit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficultyLevels);
        binding.spinnerDifficulty.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnSaveQuiz) {
            saveQuiz();
        }
    }

    private void saveQuiz() {
        String quizName = binding.etQuizName.getText().toString().trim();
        String category = binding.etQuizCategory.getText().toString().trim();
        String description = binding.etQuizDescription.getText().toString().trim();
        String difficulty = binding.spinnerDifficulty.getSelectedItem().toString();

        if (quizName.isEmpty()) {
            Toast.makeText(this, "Nama kuis tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (category.isEmpty()) {
            Toast.makeText(this, "Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Kuis berhasil disimpan", Toast.LENGTH_SHORT).show();

        finish();
    }
}