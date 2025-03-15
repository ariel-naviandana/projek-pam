package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(this);
        binding.leaderboard.setOnClickListener(this);
        binding.ecochallenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
        binding.detail1.setOnClickListener(this);
        binding.detail2.setOnClickListener(this);

        String username = getIntent().getStringExtra("USERNAME");

        TextView tvWelcome = findViewById(R.id.tvName);
        tvWelcome.setText(username);
//        if (username != null) {
//            tvWelcome.setText(username); // Menampilkan username
//        } else {
//            tvWelcome.setText(Hi user!); // Default jika username tidak ada
//        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home) {
            Toast.makeText(this, "Gunakan back untuk ke home!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.leaderboard) {
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ecochallenge) {
            // Pindah ke ChallengeActivity
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.detail1) {
            // Pindah ke ChallengeActivity
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
        } else if (id == R.id.detail2) {
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        }
    }
}
