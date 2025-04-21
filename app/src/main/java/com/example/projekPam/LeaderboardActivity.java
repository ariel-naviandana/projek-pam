package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityLeaderboardBinding;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLeaderboardBinding binding;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        leaderboardEntries.add(new LeaderboardEntry("Alice", "@alice1", 15900, 1));
        leaderboardEntries.add(new LeaderboardEntry("Bob", "@bob1", 14900, 2));
        leaderboardEntries.add(new LeaderboardEntry("Charlie", "@charlie1", 13500, 3));
        leaderboardEntries.add(new LeaderboardEntry("David", "@david1", 13000, 4));
        leaderboardEntries.add(new LeaderboardEntry("Eve", "@eve1", 12500, 5));
        leaderboardEntries.add(new LeaderboardEntry("Feri", "@feri1", 12000, 6));
        leaderboardEntries.add(new LeaderboardEntry("Geo", "@geo1", 11800, 7));
        leaderboardEntries.add(new LeaderboardEntry("Harry", "@harry1", 11500, 8));
        leaderboardEntries.add(new LeaderboardEntry("Ivy", "@ivy1", 11300, 9));
        leaderboardEntries.add(new LeaderboardEntry("Jemima", "@jem1", 10500, 10));

        adapter = new LeaderboardAdapter(leaderboardEntries);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

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
        else if (id == R.id.profile || id == R.id.avatar || id == R.id.tvName) {
            Intent intent = new Intent(this, ProfileUtamaActivity.class);
            startActivity(intent);
        }
    }
}
