package com.example.projekPam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, usernameEditText, emailEditText;
    private Button saveButton;
    private ImageView btnBack;
    private FirebaseFirestore db;
    private String currentUserId = "userId123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullNameEditText = findViewById(R.id.etFullName);
        usernameEditText = findViewById(R.id.etUsername);
        emailEditText = findViewById(R.id.etEmail);
        saveButton = findViewById(R.id.btnSimpan);
        btnBack = findViewById(R.id.btnBack);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();

        loadProfileData();

        saveButton.setOnClickListener(view -> saveProfileData());
    }

    private void loadProfileData() {
        db.collection("users").document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");

                        fullNameEditText.setText(fullName);
                        usernameEditText.setText(username);
                        emailEditText.setText(email);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal mengambil data.", Toast.LENGTH_SHORT).show());
    }

    private void saveProfileData() {
        String fullName = fullNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("fullName", fullName);
        profileData.put("username", username);
        profileData.put("email", email);
        profileData.put("xp", 25);
        profileData.put("coin", 25);

        db.collection("users").document(currentUserId)
                .set(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profil berhasil disimpan.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan profil.", Toast.LENGTH_SHORT).show());
    }
}
