package com.example.projekPam;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityChallengeBinding;

public class ChallengeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChallengeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("TITLE");
        if (title != null)
            binding.tvChallengeTitle.setText(title);

        binding.btnBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack)
            finish();
    }
}