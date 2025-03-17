package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        binding.avatar.setOnClickListener(this);
        binding.tvName.setOnClickListener(this);
        binding.detail1.setOnClickListener(this);
        binding.detail2.setOnClickListener(this);
        binding.judulAgustus.setOnClickListener(this);
        binding.judulSeptember.setOnClickListener(this);

        String username = getIntent().getStringExtra("USERNAME");
        binding.tvName.setText(username);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.home)
            Toast.makeText(this, "Anda sudah di home, gunakan back untuk ke home.", Toast.LENGTH_SHORT).show();
        else if (id == R.id.leaderboard)
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        else if (id == R.id.ecochallenge)
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        else if (id == R.id.profile || id == R.id.avatar || id == R.id.tvName) {
            String username = binding.tvName.getText().toString();
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        } else if (id == R.id.detail1 || id == R.id.judulAgustus) {
            String title = binding.judulAgustus.getText().toString();
            Intent intent = new Intent(this, ChallengeActivity.class);
            intent.putExtra("TITLE", title);
            startActivity(intent);
        } else if (id == R.id.detail2 || id == R.id.judulSeptember) {
            String title = binding.judulSeptember.getText().toString();
            Intent intent = new Intent(this, ChallengeActivity.class);
            intent.putExtra("TITLE", title);
            startActivity(intent);
        }
    }
}
