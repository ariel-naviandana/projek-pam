package com.example.projekPam;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityChallengeBinding;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChallengeBinding binding;
    private ChallengeAdapter challengeAdapter;
    private List<Challenge> listChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("TITLE");
        if (title != null)
            binding.title.setText(title);

        binding.fabAddChallenge.setOnClickListener(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listChallenge = new ArrayList<>();
        listChallenge.add(new Challenge("Agustus Challenge Mengurangi Sampah Plastik", "1 - 10 Agustus",R.drawable.poster_challenge));
        listChallenge.add(new Challenge("September Challenge Menanam Pohon", "4 - 16 September",R.drawable.poster_challenge2));

        challengeAdapter = new ChallengeAdapter(listChallenge);
        binding.rvChallenges.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChallenges.setAdapter(challengeAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            finish();
        }
        else if (id == R.id.fabAddChallenge) {
            //
        }
    }
}