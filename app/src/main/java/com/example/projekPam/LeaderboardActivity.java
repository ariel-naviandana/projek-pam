package com.example.projekPam;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityLeaderboardBinding;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private ActivityLeaderboardBinding binding;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        leaderboardEntries.add(new LeaderboardEntry("Alice", "alice1", 12900, 1));
        leaderboardEntries.add(new LeaderboardEntry("Bob", "bob1", 10900, 2));
        leaderboardEntries.add(new LeaderboardEntry("Charlie", "charlie1", 9500, 3));
        leaderboardEntries.add(new LeaderboardEntry("David", "david1", 8000, 4));
        leaderboardEntries.add(new LeaderboardEntry("Eve", "eve1", 6000, 5));

        adapter = new LeaderboardAdapter(leaderboardEntries);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        binding.btnBack.setOnClickListener(view -> finish());
    }
}
