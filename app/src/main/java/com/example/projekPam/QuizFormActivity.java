package com.example.projekPam;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityQuizFormBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizFormActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuizFormBinding binding;
    private FirebaseFirestore db;

    private ArrayList<String> materiTitles = new ArrayList<>();
    private ArrayList<String> materiIds = new ArrayList<>();
    private String selectedMateriId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        setupDifficultySpinner();
        loadMateriData();

        binding.btnBack.setOnClickListener(this);
        binding.btnSaveQuiz.setOnClickListener(this);
    }

    private void setupDifficultySpinner() {
        String[] difficultyLevels = {"Mudah", "Sedang", "Sulit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, difficultyLevels);
        binding.spinnerDifficulty.setAdapter(adapter);
    }

    private void loadMateriData() {
        // Fetch materi data from Firestore
        db.collection("materi")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String materiId = document.getId();
                            String materiTitle = document.getString("judul");

                            materiIds.add(materiId);
                            materiTitles.add(materiTitle);
                        }

                        // Set up the dropdown (Spinner) for Materi
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, materiTitles);
                        binding.spinnerCategory.setAdapter(adapter);

                        // Handle selection
                        binding.spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                                selectedMateriId = materiIds.get(position);
                            }

                            @Override
                            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                                selectedMateriId = null;
                            }
                        });
                    } else {
                        Toast.makeText(this, "Gagal memuat data materi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnSaveQuiz) {
            saveQuizToFirestore();
        }
    }

    private void saveQuizToFirestore() {
        String quizName = binding.etQuizName.getText().toString().trim();
        String description = binding.etQuizDescription.getText().toString().trim();
        String difficulty = binding.spinnerDifficulty.getSelectedItem().toString();

        if (quizName.isEmpty()) {
            Toast.makeText(this, "Nama kuis tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedMateriId == null) {
            Toast.makeText(this, "Silakan pilih kategori materi", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> quiz = new HashMap<>();
        quiz.put("judul", quizName);
        quiz.put("id_materi", selectedMateriId); // Save selected materi ID
        quiz.put("deskripsi", description);
        quiz.put("kesulitan", difficulty);

        db.collection("kuis")
                .add(quiz)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Kuis berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal menyimpan kuis", Toast.LENGTH_SHORT).show();
                });
    }
}