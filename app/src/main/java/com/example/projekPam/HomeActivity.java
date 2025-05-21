package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerViewMateri, recyclerViewChallenge;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userRole = "user"; // Default role
    private MateriHomeAdapter materiAdapter;
    private ChallengeHomeAdapter challengeAdapter;
    private List<Materi> materiList;
    private List<Challenge> challengeList;
    private com.example.projekPam.databinding.ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.projekPam.databinding.ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerViews
        recyclerViewMateri = binding.recyclerViewMateri;
        recyclerViewChallenge = binding.recyclerViewChallenge;

        // Setup RecyclerViews
        materiList = new ArrayList<>();
        challengeList = new ArrayList<>();

        materiAdapter = new MateriHomeAdapter(this, materiList);
        recyclerViewMateri.setLayoutManager(new GridLayoutManager(this, 3)); // 3 items per row
        recyclerViewMateri.setAdapter(materiAdapter);

        challengeAdapter = new ChallengeHomeAdapter(this, challengeList);
        recyclerViewChallenge.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewChallenge.setAdapter(challengeAdapter);

        // Load user role and setup UI
        loadUserRole();
    }

    private void loadUserRole() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        com.google.firebase.firestore.DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userRole = document.getString("role") != null ? document.getString("role") : "user";
                            String username = document.getString("username");
                            String image = document.getString("image");
                            Long xp = document.getLong("xp");
                            Long coin = document.getLong("coin");
                            binding.tvName.setText(username != null ? username : "User");
                            binding.exp25.setText(xp != null ? String.valueOf(xp) : "0");
                            binding.tvLives.setText(coin != null ? String.valueOf(coin) : "0");
                            if (image != null && !image.isEmpty()) {
                                Glide.with(this)
                                        .load(image)
                                        .placeholder(R.drawable.avatar)
                                        .error(R.drawable.avatar)
                                        .into(binding.avatar);
                            } else {
                                binding.avatar.setImageResource(R.drawable.avatar);
                            }
                        } else {
                            userRole = "user";
                            binding.tvName.setText("User");
                            binding.exp25.setText("0");
                            binding.tvLives.setText("0");
                            Toast.makeText(this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        userRole = "user";
                        binding.tvName.setText("User");
                        binding.exp25.setText("0");
                        binding.tvLives.setText("0");
                        Toast.makeText(this, "Gagal mengambil data pengguna: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    setupNavbar();
                    setupClickListeners();
                    loadMateriData();
                    loadChallengeData();
                });
    }

    private void loadMateriData() {
        db.collection("materi")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    materiList.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot) {
                        String id = doc.getId();
                        String judul = doc.getString("judul");
                        String image = doc.getString("image");
                        String deskripsi = doc.getString("deskripsi");
                        com.google.firebase.Timestamp createdAt = doc.getTimestamp("created_at");

                        // Validate required fields
                        if (judul == null || judul.isEmpty() || createdAt == null) {
                            Log.w("HomeActivity", "Skipping materi document with missing required fields: " + id);
                            continue;
                        }

                        Materi materi = new Materi(id, judul, image, deskripsi, createdAt);
                        materiList.add(materi);
                    }
                    materiAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeActivity", "Failed to load materi: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat materi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadChallengeData() {
        db.collection("challenge")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(6)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    challengeList.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot) {
                        String id = doc.getId();
                        String judul = doc.getString("judul");
                        String image = doc.getString("image");
                        com.google.firebase.Timestamp dateStart = doc.getTimestamp("date_start");
                        com.google.firebase.Timestamp dateEnd = doc.getTimestamp("date_end");
                        com.google.firebase.Timestamp createdAt = doc.getTimestamp("created_at");

                        // Validate required fields
                        if (judul == null || judul.isEmpty() || dateStart == null || dateEnd == null || createdAt == null) {
                            Log.w("HomeActivity", "Skipping challenge document with missing required fields: " + id);
                            continue;
                        }

                        Challenge challenge = new Challenge(id, judul, image, dateStart, dateEnd, createdAt);
                        challengeList.add(challenge);
                    }
                    challengeAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("HomeActivity", "Failed to load challenges: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat challenges: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupNavbar() {
        // Show appropriate navbar based on user role
        binding.Navbar.setVisibility(userRole.equals("user") ? View.VISIBLE : View.GONE);
        binding.NavbarAdmin.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
    }

    private void setupClickListeners() {
        // Common click listeners
        binding.notif.setOnClickListener(this);
        binding.avatar.setOnClickListener(this);
        binding.tvName.setOnClickListener(this);

        // Navbar for regular users
        if (userRole.equals("user")) {
            binding.home.setOnClickListener(this);
            binding.leaderboard.setOnClickListener(this);
            binding.ecochallenge.setOnClickListener(this);
            binding.profile.setOnClickListener(this);
        }

        // Navbar for admins
        if (userRole.equals("admin")) {
            binding.homeAdmin.setOnClickListener(this);
            binding.materi.setOnClickListener(this);
            binding.quiz.setOnClickListener(this);
            binding.challenge.setOnClickListener(this);
            binding.profileAdmin.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home || id == R.id.homeAdmin) {
            Toast.makeText(this, "Anda sudah di home.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leaderboard) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.ecochallenge || id == R.id.challenge) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.profile || id == R.id.profileAdmin || id == R.id.avatar || id == R.id.tvName) {
            String username = binding.tvName.getText().toString();
            Intent intent = new Intent(this, ProfileUtamaActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.materi) {
            Intent intent = new Intent(this, MateriActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.quiz) {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.notif) {
            mAuth.signOut();
            Toast.makeText(this, "Berhasil logout!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}