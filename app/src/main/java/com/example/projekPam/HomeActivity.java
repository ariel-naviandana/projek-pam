package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set username from current Firebase user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (email != null) {
                binding.tvName.setText(email);
            } else {
                binding.tvName.setText("User"); // Fallback if email is null
            }
        } else {
            binding.tvName.setText("Guest"); // Fallback if no user is logged in
        }

        // Set click listeners
        binding.home.setOnClickListener(this);
        binding.leaderboard.setOnClickListener(this);
        binding.ecochallenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
        binding.avatar.setOnClickListener(this);
        binding.tvName.setOnClickListener(this);
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
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.profile || id == R.id.avatar || id == R.id.tvName) {
            String username = binding.tvName.getText().toString();
            Intent intent = new Intent(this, ProfileUtamaActivity.class);
            intent.putExtra("USERNAME", username);
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
        }
    }
}