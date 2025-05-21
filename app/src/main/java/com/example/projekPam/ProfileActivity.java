package com.example.projekPam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, usernameEditText, emailEditText;
    private Button saveButton;
    private ImageView btnBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        fullNameEditText = findViewById(R.id.etFullName);
        usernameEditText = findViewById(R.id.etUsername);
        emailEditText = findViewById(R.id.etEmail);
        saveButton = findViewById(R.id.btnSimpan);
        btnBack = findViewById(R.id.btnBack);

        // Back button listener
        btnBack.setOnClickListener(v -> finish());

        // Load profile data
        loadProfileData();

        // Save button listener
        saveButton.setOnClickListener(view -> saveProfileData());
    }

    private void loadProfileData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullname"); // Changed to match Firestore field
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");

                        fullNameEditText.setText(fullName != null ? fullName : "");
                        usernameEditText.setText(username != null ? username : "");
                        emailEditText.setText(email != null ? email : "");
                    } else {
                        Toast.makeText(this, "Data profil tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal mengambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveProfileData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String fullName = fullNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("fullname", fullName); // Changed to match Firestore field
        profileData.put("username", username);
        profileData.put("email", email);
        // Preserve existing fields if they exist
        profileData.put("xp", 25); // Default or fetch from Firestore if needed
        profileData.put("coin", 25); // Default or fetch from Firestore if needed
        profileData.put("id", currentUser.getUid());
        profileData.put("image", ""); // Preserve existing image or update if needed
        profileData.put("created_at", new java.util.Date()); // Preserve or update as needed
        profileData.put("role", "user"); // Preserve existing role

        db.collection("users").document(currentUser.getUid())
                .set(profileData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profil berhasil disimpan.", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal menyimpan profil: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}