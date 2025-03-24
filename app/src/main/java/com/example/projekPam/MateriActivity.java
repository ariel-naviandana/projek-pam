package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projekPam.databinding.ActivityMateriBinding;

public class MateriActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityMateriBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBackMateri.setOnClickListener(this);

        String title = getIntent().getStringExtra("TITLE");
        binding.tvCardJudulMateri.setText(title);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBackMateri)
            finish();
    }
}