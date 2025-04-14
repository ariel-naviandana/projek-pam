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
            binding.tvChallengeTitle.setText(title);

        binding.btnBack.setOnClickListener(this);
        binding.fabTambahChallenge.setOnClickListener(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listChallenge = new ArrayList<>();
        listChallenge.add(new Materi("Gas Rumah Kaca dan Dampaknya", R.drawable.fire_icon));
        listChallenge.add(new Materi("Bukti Perubahan Iklim pada Ekosistem", R.drawable.ic_earth));
        listChallenge.add(new Materi("Energi Terbarukan dan Perubahan Iklim", R.drawable.ic_electricity));
        listChallenge.add(new Materi("Adaptasi dan Mitigasi Perubahan Iklim", R.drawable.ic_umbrella));
        listChallenge.add(new Materi("Perubahan Iklim dan Pertanian", R.drawable.ic_ecofriendly));

        challengeAdapter = new MateriAdapter(listChallenge);
        binding.rvDaftarChallenge.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDaftarChallenge.setAdapter(challengeAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            finish();
        }
        else if (id == R.id.fabTambahChallenge) {
            //
        }
    }
}