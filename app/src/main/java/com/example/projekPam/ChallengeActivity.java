package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityChallengeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChallengeBinding binding;
    private ChallengeAdapter adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Challenge> challengeList = new ArrayList<>();
    private String userRole = "user"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupRecyclerView();
        loadUserRole();
        loadChallenges();

        binding.fabAddChallenge.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        adapter = new ChallengeAdapter(challengeList, this, db, userRole);
        binding.rvChallenges.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChallenges.setAdapter(adapter);
    }

    private void loadUserRole() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRole = documentSnapshot.getString("role") != null ? documentSnapshot.getString("role") : "user";
                        setupNavbar();
                        // Update adapter with user role to control edit/delete visibility
                        adapter.setUserRole(userRole);
                        adapter.notifyDataSetChanged();
                        // Hide FAB for non-admin users
                        binding.fabAddChallenge.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
                    } else {
                        Toast.makeText(this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        userRole = "user";
                        setupNavbar();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Gagal mengambil role pengguna: " + e.getMessage());
                    Toast.makeText(this, "Gagal mengambil data pengguna.", Toast.LENGTH_SHORT).show();
                    userRole = "user";
                    setupNavbar();
                });
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

    private void setupNavbar() {
        // Show appropriate navbar based on user role
        binding.Navbar.setVisibility(userRole.equals("user") ? View.VISIBLE : View.GONE);
        binding.NavbarAdmin.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);

        // Navbar for regular users
        if (userRole.equals("user")) {
            binding.home.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.leaderboard.setOnClickListener(v -> {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.ecochallenge.setOnClickListener(v -> {
                Toast.makeText(this, "Anda sudah di halaman EcoChallenge.", Toast.LENGTH_SHORT).show();
            });

            binding.profile.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileUtamaActivity.class);
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            intent.putExtra("USERNAME", username != null ? username : "User");
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        });
            });
        }

        // Navbar for admins
        if (userRole.equals("admin")) {
            binding.homeAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.materi.setOnClickListener(v -> {
                Intent intent = new Intent(this, MateriActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.quiz.setOnClickListener(v -> {
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.challenge.setOnClickListener(v -> {
                Toast.makeText(this, "Anda sudah di halaman EcoChallenge.", Toast.LENGTH_SHORT).show();
            });

            binding.profileAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileUtamaActivity.class);
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            intent.putExtra("USERNAME", username != null ? username : "User");
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        });
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadChallenges(); // Refresh challenges after adding/editing
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabAddChallenge) {
            Intent intent = new Intent(this, ChallengeFormActivity.class);
            startActivity(intent);
        }
    }
}