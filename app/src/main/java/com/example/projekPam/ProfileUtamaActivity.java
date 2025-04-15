package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityProfileUtamaBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileUtamaActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProfileUtamaBinding binding;
    private ProfileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUtamaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<DaftarTemanEntry> DaftarTemanEntries = new ArrayList<>();
        DaftarTemanEntries.add(new DaftarTemanEntry("Alice", "@alice1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Bob", "@bob1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Charlie", "@charlie1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("David", "@david1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Eve", "@eve1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Feri", "@feri1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Geo", "@geo1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Harry", "@harry1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Ivy", "@ivy1"));
        DaftarTemanEntries.add(new DaftarTemanEntry("Jemima", "@jem1"));

        adapter = new ProfileAdapter(DaftarTemanEntries);

        binding.recyclerViewDaftarTeman.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewDaftarTeman.setAdapter(adapter);

        binding.btnEditProfile.setOnClickListener(this);
        binding.home.setOnClickListener(this);
        binding.leaderboard.setOnClickListener(this);
        binding.ecochallenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.leaderboard) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.ecochallenge) {
            Toast.makeText(this, "Halaman belum ada", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.btnEditProfile) {
            String username = "@username";
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        }
    }
}
