package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityChallengeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity {
    private ActivityChallengeBinding binding;
    private ChallengeAdapter adapter;
    private FirebaseFirestore db;
    private List<Challenge> challengeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        loadChallenges();
        setupClickListeners();
        setupNavbar();
    }

    private void setupRecyclerView() {
        adapter = new ChallengeAdapter(challengeList,this, db);
        binding.rvChallenges.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChallenges.setAdapter(adapter);
    }

    private void loadChallenges() {
        db.collection("challenge")
                .orderBy("date_start", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("FirestoreError", "Listen failed.", error);
                        return;
                    }

                    if (value != null) {
                        List<Challenge> newChallenges = new ArrayList<>();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            try {
                                Challenge challenge = doc.toObject(Challenge.class);
                                if (challenge != null) {
                                    challenge.setId(doc.getId());

                                    if (doc.get("date_start") instanceof com.google.firebase.Timestamp) {
                                        challenge.setDate_start(doc.getTimestamp("date_start"));
                                    }
                                    if (doc.get("date_end") instanceof com.google.firebase.Timestamp) {
                                        challenge.setDate_end(doc.getTimestamp("date_end"));
                                    }

                                    newChallenges.add(challenge);
                                }
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error mapping document", e);
                            }
                        }
                        updateChallengeList(newChallenges);
                    }
                });
    }

    private void updateChallengeList(List<Challenge> newChallenges) {
        List<Challenge> finalList = new ArrayList<>(newChallenges);

        runOnUiThread(() -> {
            challengeList.clear();
            challengeList.addAll(finalList);
            adapter.notifyDataSetChanged();
        });
    }

    private void setupClickListeners() {
        binding.fabAddChallenge.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, ChallengeFormActivity.class), 1);
        });
    }

    private void setupNavbar() {
        binding.home.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        binding.materi.setOnClickListener(v -> {
            // Intent ke MateriActivity
            Toast.makeText(this, "Materi clicked", Toast.LENGTH_SHORT).show();
        });

        binding.quiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        });

        binding.challenge.setOnClickListener(v -> {
            Toast.makeText(this, "Anda sudah di halaman EcoChallenge.", Toast.LENGTH_SHORT).show();
        });

        binding.profile.setOnClickListener(v -> {
            // Intent ke ProfileActivity
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
        });
    }
}