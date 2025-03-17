package com.example.projekPam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityHeadBinding;

public class HeadActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHeadBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cbRambut.setOnClickListener(this);
        binding.cbAlis.setOnClickListener(this);
        binding.cbKumis.setOnClickListener(this);
        binding.cbJanggut.setOnClickListener(this);
        binding.btnCall.setOnClickListener(this);

        String username = getIntent().getStringExtra("EMAIL");
        binding.tvUsername.setText("Welcome, " + username);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cbRambut)
            toggleVisibility(binding.imgRambut, binding.cbRambut.isChecked());
        else if (id == R.id.cbAlis)
            toggleVisibility(binding.imgAlis, binding.cbAlis.isChecked());
        else if (id == R.id.cbKumis)
            toggleVisibility(binding.imgKumis, binding.cbKumis.isChecked());
        else if (id == R.id.cbJanggut)
            toggleVisibility(binding.imgJanggut, binding.cbJanggut.isChecked());
        else if (id == R.id.btnCall) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        }
    }

    private void toggleVisibility(View view, boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
