package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSimpan.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);

        String username = getIntent().getStringExtra("USERNAME");
        if (username != null)
            binding.etUsername.setText(username);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSimpan) {
            String username = binding.etUsername.getText().toString();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        } else if (id == R.id.btnBack)
            finish();
    }
}