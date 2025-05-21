package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileUtamaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFriends, recyclerViewAddFriends;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<Friend> friendsList;
    private List<Friend> potentialFriendsList;
    private Button editProfileButton;
    private TextView usernameText, emailText, xpText, coinText;
    private ImageView avatarImage, ivXp, ivKoin;
    private FriendAdapter friendAdapter;
    private AddFriendAdapter addFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_utama);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        recyclerViewFriends = findViewById(R.id.recyclerViewDaftarTeman);
        recyclerViewAddFriends = findViewById(R.id.recyclerViewTambahTeman);
        editProfileButton = findViewById(R.id.btnEditProfile);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        xpText = findViewById(R.id.exp25);
        coinText = findViewById(R.id.tvLives);
        avatarImage = findViewById(R.id.ivAvatar);
        ivXp = findViewById(R.id.ivXp);
        ivKoin = findViewById(R.id.ivKoin);

        friendsList = new ArrayList<>();
        potentialFriendsList = new ArrayList<>();

        // Initialize adapters
        friendAdapter = new FriendAdapter(this, friendsList, getCurrentUserId());
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewFriends.setAdapter(friendAdapter);

        addFriendAdapter = new AddFriendAdapter(this, potentialFriendsList, getCurrentUserId());
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAddFriends.setAdapter(addFriendAdapter);

        // Edit profile button listener
        editProfileButton.setOnClickListener(view -> {
            startActivity(new Intent(ProfileUtamaActivity.this, ProfileActivity.class));
        });

        // Set username from Intent if available
        Intent intent = getIntent();
        if (intent.hasExtra("USERNAME")) {
            usernameText.setText(intent.getStringExtra("USERNAME"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
        loadFriendsList();
        loadPotentialFriendsList();
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            finish();
            return "";
        }
        return currentUser.getUid();
    }

    private void loadProfile() {
        String userId = getCurrentUserId();
        if (userId.isEmpty()) return;

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        Long xp = documentSnapshot.getLong("xp");
                        Long coin = documentSnapshot.getLong("coin");

                        usernameText.setText(username != null ? username : "-");
                        emailText.setText(email != null ? email : "-");
                        xpText.setText(xp != null ? String.valueOf(xp) : "0");
                        coinText.setText(coin != null ? String.valueOf(coin) : "0");

                        avatarImage.setImageResource(R.drawable.avatar);
                    } else {
                        Log.e("ProfileLoad", "Dokumen profil tidak ditemukan");
                        usernameText.setText("-");
                        emailText.setText("-");
                        xpText.setText("0");
                        coinText.setText("0");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileLoad", "Gagal memuat profil: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat profil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadFriendsList() {
        String userId = getCurrentUserId();
        if (userId.isEmpty()) return;

        db.collection("users").document(userId)
                .collection("friends")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("FriendLoad", "Listen failed: " + e.getMessage());
                        Toast.makeText(this, "Gagal memuat daftar teman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    friendsList.clear();
                    for (DocumentSnapshot document : snapshots) {
                        String id = document.getId();
                        String username = document.getString("username");
                        String fullname = document.getString("fullname");

                        friendsList.add(new Friend(id, username, fullname));
                    }
                    friendAdapter.notifyDataSetChanged();
                });
    }

    public void addFriendToFriendsList(Friend friend) {
        potentialFriendsList.remove(friend);
        friendAdapter.notifyDataSetChanged();
        addFriendAdapter.notifyDataSetChanged();
    }

    public void addPotentialFriendBack(Friend friend) {
        potentialFriendsList.add(friend);
        addFriendAdapter.notifyDataSetChanged();
    }

    private void loadPotentialFriendsList() {
        String userId = getCurrentUserId();
        if (userId.isEmpty()) return;

        db.collection("users").document(userId)
                .collection("friends")
                .get()
                .addOnSuccessListener(friendSnapshots -> {
                    List<String> friendIds = new ArrayList<>();
                    for (DocumentSnapshot doc : friendSnapshots) {
                        friendIds.add(doc.getId());
                    }

                    db.collection("users")
                            .get()
                            .addOnSuccessListener(userSnapshots -> {
                                potentialFriendsList.clear();
                                for (DocumentSnapshot document : userSnapshots) {
                                    String id = document.getId();
                                    if (!id.equals(userId) && !friendIds.contains(id)) {
                                        String username = document.getString("username");
                                        String fullname = document.getString("fullname");
                                        potentialFriendsList.add(new Friend(id, username, fullname));
                                    }
                                }
                                addFriendAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("PotentialLoad", "Gagal memuat daftar calon teman: " + e.getMessage());
                                Toast.makeText(this, "Gagal memuat daftar calon teman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("FriendCheck", "Gagal mengambil daftar teman: " + e.getMessage());
                    Toast.makeText(this, "Gagal mengambil daftar teman: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}