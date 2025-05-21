package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileUtamaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFriends, recyclerViewAddFriends;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<Friend> friendsList;
    private List<Friend> potentialFriendsList;
    private Button editProfileButton;
    private TextView usernameText, emailText, xpText, coinText, txtTambahTeman, txtDaftarTeman;
    private ImageView avatarImage, ivXp, ivKoin;
    private FriendAdapter friendAdapter;
    private AddFriendAdapter addFriendAdapter;
    private String userRole = "user"; // Default role

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
        txtTambahTeman = findViewById(R.id.txtTambahTeman);
        txtDaftarTeman = findViewById(R.id.txtDaftarTeman);

        friendsList = new ArrayList<>();
        potentialFriendsList = new ArrayList<>();

        // Initialize adapters
        friendAdapter = new FriendAdapter(this, friendsList, getCurrentUserId());
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewFriends.setAdapter(friendAdapter);

        addFriendAdapter = new AddFriendAdapter(this, potentialFriendsList, getCurrentUserId());
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAddFriends.setAdapter(addFriendAdapter);

        // Load user role and setup UI
        loadUserRole();

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
        if (userRole.equals("user")) {
            loadFriendsList();
            loadPotentialFriendsList();
        }
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return "";
        }
        return currentUser.getUid();
    }

    private void loadUserRole() {
        String userId = getCurrentUserId();
        if (userId.isEmpty()) return;

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRole = documentSnapshot.getString("role") != null ? documentSnapshot.getString("role") : "user";
                        // Show/hide friend-related views based on role
                        int visibility = userRole.equals("admin") ? View.GONE : View.VISIBLE;
                        recyclerViewFriends.setVisibility(visibility);
                        recyclerViewAddFriends.setVisibility(visibility);
                        txtTambahTeman.setVisibility(visibility);
                        txtDaftarTeman.setVisibility(visibility);
                        // Setup navbar after role is determined
                        setupNavbar();
                    } else {
                        userRole = "user";
                        setupNavbar();
                        Toast.makeText(this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Gagal mengambil role pengguna: " + e.getMessage());
                    userRole = "user";
                    setupNavbar();
                    Toast.makeText(this, "Gagal mengambil data pengguna.", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupNavbar() {
        // Show appropriate navbar based on user role
        findViewById(R.id.Navbar).setVisibility(userRole.equals("user") ? View.VISIBLE : View.GONE);
        findViewById(R.id.NavbarAdmin).setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);

        // Navbar for regular users
        if (userRole.equals("user")) {
            findViewById(R.id.home).setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.leaderboard).setOnClickListener(v -> {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.ecochallenge).setOnClickListener(v -> {
                Intent intent = new Intent(this, ChallengeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.profile).setOnClickListener(v -> {
                Toast.makeText(this, "Anda sudah di halaman profil.", Toast.LENGTH_SHORT).show();
            });
        }

        // Navbar for admins
        if (userRole.equals("admin")) {
            findViewById(R.id.homeAdmin).setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.materi).setOnClickListener(v -> {
                Intent intent = new Intent(this, MateriActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.quiz).setOnClickListener(v -> {
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.challenge).setOnClickListener(v -> {
                Intent intent = new Intent(this, ChallengeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            findViewById(R.id.profileAdmin).setOnClickListener(v -> {
                Toast.makeText(this, "Anda sudah di halaman profil.", Toast.LENGTH_SHORT).show();
            });
        }

        // Edit profile button listener (common for both roles)
        editProfileButton.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileUtamaActivity.this, ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
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
                        String image = documentSnapshot.getString("image");

                        usernameText.setText(username != null ? username : "-");
                        emailText.setText(email != null ? email : "-");
                        xpText.setText(xp != null ? String.valueOf(xp) : "0");
                        coinText.setText(coin != null ? String.valueOf(coin) : "0");

                        if (image != null && !image.isEmpty()) {
                            Glide.with(this)
                                    .load(image)
                                    .placeholder(R.drawable.avatar)
                                    .error(R.drawable.avatar)
                                    .into(avatarImage);
                        } else {
                            avatarImage.setImageResource(R.drawable.avatar);
                        }
                    } else {
                        Log.e("ProfileLoad", "Dokumen profil tidak ditemukan");
                        usernameText.setText("-");
                        emailText.setText("-");
                        xpText.setText("0");
                        coinText.setText("0");
                        avatarImage.setImageResource(R.drawable.avatar);
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
                        String image = document.getString("image");

                        friendsList.add(new Friend(id, username, fullname, image));
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
                                        String image = document.getString("image");

                                        potentialFriendsList.add(new Friend(id, username, fullname, image));
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