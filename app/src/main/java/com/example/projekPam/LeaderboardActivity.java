package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityLeaderboardBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLeaderboardBinding binding;
    private LeaderboardAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and Adapter
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        adapter = new LeaderboardAdapter(leaderboardEntries);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Load leaderboard data from Firestore
        loadLeaderboardData();

        // Set click listeners for navigation
        binding.home.setOnClickListener(this);
        binding.leaderboard.setOnClickListener(this);
        binding.ecochallenge.setOnClickListener(this);
        binding.profile.setOnClickListener(this);
    }

    private void loadLeaderboardData() {
        db.collection("users")
                .whereEqualTo("role", "user") // Filter for users with role "user"
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot) {
                        String fullname = doc.getString("fullname");
                        String username = doc.getString("username");
                        Long xp = doc.getLong("xp"); // Firestore may store xp as Long
                        String role = doc.getString("role");

                        // Validate required fields
                        if (fullname == null || fullname.isEmpty() || username == null || username.isEmpty() || xp == null || role == null) {
                            Log.w("LeaderboardActivity", "Skipping user document with missing required fields: " + doc.getId());
                            continue;
                        }

                        // Create LeaderboardEntry without ranking (to be assigned after sorting)
                        LeaderboardEntry entry = new LeaderboardEntry(fullname, username, xp.intValue(), 0); // Temporary ranking
                        leaderboardEntries.add(entry);
                    }

                    // Sort manually by xp in descending order
                    Collections.sort(leaderboardEntries, new Comparator<LeaderboardEntry>() {
                        @Override
                        public int compare(LeaderboardEntry e1, LeaderboardEntry e2) {
                            return Integer.compare(e2.getSkor(), e1.getSkor()); // Descending order
                        }
                    });

                    // Assign rankings based on sorted position
                    for (int i = 0; i < leaderboardEntries.size(); i++) {
                        leaderboardEntries.get(i).setRanking(i + 1); // Ranking starts at 1
                    }

                    // Update adapter with sorted data
                    adapter.updateData(leaderboardEntries);

                    // Notify user if no data is found
                    if (leaderboardEntries.isEmpty()) {
                        Toast.makeText(this, "No users found for the leaderboard", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LeaderboardActivity", "Failed to load leaderboard data: " + e.getMessage());
                    Toast.makeText(this, "Failed to load leaderboard: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.leaderboard) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.ecochallenge) {
            Intent intent = new Intent(this, ChallengeActivity.class);
            startActivity(intent);
        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileUtamaActivity.class);
            startActivity(intent);
        }
    }
}