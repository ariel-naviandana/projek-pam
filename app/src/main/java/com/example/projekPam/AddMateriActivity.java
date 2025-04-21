package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityAddMateriBinding;

public class AddMateriActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddMateriBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSubmitMateri.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmitMateri) {
            Intent intent = new Intent(this, MateriActivity.class);
            startActivity(intent);
            finish();
        }
    }
}