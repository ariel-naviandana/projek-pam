package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userRole = "user"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userRole = document.getString("role") != null ? document.getString("role") : "user";
                            String username = document.getString("username");
                            Long xp = document.getLong("xp");
                            Long coin = document.getLong("coin");
                            binding.tvName.setText(username != null ? username : "User");
                            binding.exp25.setText(xp != null ? String.valueOf(xp) : "0");
                            binding.tvLives.setText(coin != null ? String.valueOf(coin) : "0");
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
                });
    }

    private void setupNavbar() {
        // Show appropriate navbar based on user role
        binding.Navbar.setVisibility(userRole.equals("user") ? View.VISIBLE : View.GONE);
        binding.NavbarAdmin.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
    }

    private void setupClickListeners() {
        // Common click listeners
        binding.detail1.setOnClickListener(this);
        binding.detail2.setOnClickListener(this);
        binding.judulAgustus.setOnClickListener(this);
        binding.judulSeptember.setOnClickListener(this);
        binding.pengantar.setOnClickListener(this);
        binding.gasRumahKaca.setOnClickListener(this);
        binding.perubahanIklim.setOnClickListener(this);
        binding.energiTerbarukan.setOnClickListener(this);
        binding.adaptasiPerubahan.setOnClickListener(this);
        binding.sampah.setOnClickListener(this);
        binding.notif.setOnClickListener(this);

        // Navbar for regular users
        if (userRole.equals("user")) {
            binding.home.setOnClickListener(this);
            binding.leaderboard.setOnClickListener(this);
            binding.ecochallenge.setOnClickListener(this);
            binding.profile.setOnClickListener(this);
            binding.avatar.setOnClickListener(this);
            binding.tvName.setOnClickListener(this);
        }

        // Navbar for admins
        if (userRole.equals("admin")) {
            binding.homeAdmin.setOnClickListener(this);
            binding.materi.setOnClickListener(this);
            binding.quiz.setOnClickListener(this);
            binding.challenge.setOnClickListener(this);
            binding.profileAdmin.setOnClickListener(this);
            // Admin can also access profile via avatar and tvName
            binding.avatar.setOnClickListener(this);
            binding.tvName.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home) {
            Toast.makeText(this, "Anda sudah di home, gunakan back untuk ke home.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leaderboard) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.ecochallenge) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.profile || id == R.id.avatar || id == R.id.tvName || id == R.id.profileAdmin) {
            String username = binding.tvName.getText().toString();
            Intent intent = new Intent(this, ProfileUtamaActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.homeAdmin) {
            Toast.makeText(this, "Anda sudah di home, gunakan back untuk ke home.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.materi) {
            Intent intent = new Intent(this, MateriActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.quiz) {
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.challenge) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.pengantar) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Pengantar");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.gasRumahKaca) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Gas Rumah Kaca");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.perubahanIklim) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Perubahan Iklim");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.energiTerbarukan) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Energi Terbarukan");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.adaptasiPerubahan) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Adaptasi Perubahan");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.sampah) {
            Intent intent = new Intent(this, MateriActivity.class);
            intent.putExtra("TITLE", "Sampah");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.notif) {
            // Sign out from Firebase
            mAuth.signOut();
            Toast.makeText(this, "Berhasil logout!", Toast.LENGTH_SHORT).show();
            // Redirect to LoginActivity and clear activity stack
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (id == R.id.detail1 || id == R.id.judulAgustus) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            intent.putExtra("CHALLENGE", "Agustus Challenge Mengurangi Sampah Plastik");
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else if (id == R.id.detail2 || id == R.id.judulSeptember) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            intent.putExtra("CHALLENGE", "September Challenge Menanam Pohon");
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}