package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileUtamaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFriends, recyclerViewAddFriends;
    private FirebaseFirestore db;
    private String currentUserId = "userId123";
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

        db = FirebaseFirestore.getInstance();
        friendsList = new ArrayList<>();
        potentialFriendsList = new ArrayList<>();

        friendAdapter = new FriendAdapter(this, friendsList, currentUserId);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewFriends.setAdapter(friendAdapter);

        addFriendAdapter = new AddFriendAdapter(this, potentialFriendsList, currentUserId);
        recyclerViewAddFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAddFriends.setAdapter(addFriendAdapter);

        editProfileButton.setOnClickListener(view -> {
            startActivity(new Intent(ProfileUtamaActivity.this, ProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
        loadFriendsList();
        loadPotentialFriendsList();
    }

    private void loadProfile() {
        db.collection("users").document(currentUserId)
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
                    }
                })
                .addOnFailureListener(e -> Log.e("ProfileLoad", "Gagal memuat profil: " + e.getMessage()));
    }

    private void loadFriendsList() {
        db.collection("users").document(currentUserId)
                .collection("friends")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("FriendLoad", "Listen failed.", e);
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
        db.collection("users").document(currentUserId)
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

                                    if (!id.equals(currentUserId) && !friendIds.contains(id)) {
                                        String username = document.getString("username");
                                        String fullname = document.getString("fullname");
                                        potentialFriendsList.add(new Friend(id, username, fullname));
                                    }
                                }
                                addFriendAdapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Log.e("PotentialLoad", "Gagal memuat daftar calon teman: " + e.getMessage()));
                })
                .addOnFailureListener(e -> Log.e("FriendCheck", "Gagal mengambil daftar teman: " + e.getMessage()));
    }
}