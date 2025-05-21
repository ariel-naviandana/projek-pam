package com.example.projekPam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.projekPam.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl;
    private boolean isCloudinaryInitialized = false;
    private boolean isUploadingImage = false; // Track upload status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Cloudinary
        initCloudinary();

        // Load current user profile data
        loadProfile();

        // Set click listeners
        binding.btnBack.setOnClickListener(v -> finish());
        binding.editGambar.setOnClickListener(v -> openImagePicker());
        binding.btnSimpan.setOnClickListener(v -> saveProfile());
    }

    private void initCloudinary() {
        if (!isCloudinaryInitialized) {
            try {
                Map<String, String> config = new HashMap<>();
                config.put("cloud_name", "dto6d9tbe"); // Replace with your Cloudinary cloud name
                MediaManager.init(this, config);
                isCloudinaryInitialized = true;
            } catch (IllegalStateException e) {
                Log.e("Cloudinary", "MediaManager already initialized: " + e.getMessage());
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Preview the selected image without uploading
            Glide.with(this)
                    .load(imageUri)
                    .into(binding.gambar);
        }
    }

    private void loadProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullname = documentSnapshot.getString("fullname");
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        String image = documentSnapshot.getString("image");

                        binding.etFullName.setText(fullname != null ? fullname : "");
                        binding.etUsername.setText(username != null ? username : "");
                        binding.etEmail.setText(email != null ? email : "");

                        if (image != null && !image.isEmpty()) {
                            Glide.with(this)
                                    .load(image)
                                    .placeholder(R.drawable.avatar)
                                    .error(R.drawable.avatar)
                                    .into(binding.gambar);
                            imageUrl = image; // Preserve existing image URL
                        } else {
                            binding.gambar.setImageResource(R.drawable.avatar);
                        }
                    } else {
                        Toast.makeText(this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileLoad", "Gagal memuat profil: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat profil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveProfile() {
        // Check if an image upload is in progress
        if (isUploadingImage) {
            Toast.makeText(this, "Harap tunggu hingga gambar selesai diunggah.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        String fullname = binding.etFullName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();

        // Validation
        if (fullname.isEmpty()) {
            binding.etFullName.setError("Nama lengkap tidak boleh kosong");
            return;
        }
        if (username.isEmpty()) {
            binding.etUsername.setError("Username tidak boleh kosong");
            return;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email tidak boleh kosong");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Masukkan email yang valid");
            return;
        }

        // Prepare data to save
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullname", fullname);
        userData.put("username", username);
        userData.put("email", email);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            userData.put("image", imageUrl);
        }

        // Handle image upload if a new image is selected
        if (imageUri != null) {
            uploadImageToCloudinary(userData);
        } else {
            updateFirestore(userData);
        }
    }

    private void uploadImageToCloudinary(Map<String, Object> userData) {
        binding.editGambar.setEnabled(false);
        binding.btnSimpan.setEnabled(false);
        isUploadingImage = true;

        MediaManager.get().upload(imageUri)
                .unsigned("ecokids") // Replace with your Cloudinary preset
                .option("resource_type", "image")
                .option("quality", "80") // Compress image quality
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Starting image upload");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        userData.put("image", imageUrl);
                        updateFirestore(userData);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        binding.editGambar.setEnabled(true);
                        binding.btnSimpan.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(ProfileActivity.this, "Gagal mengunggah gambar: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        // Use existing image or proceed without image
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            userData.put("image", imageUrl);
                        }
                        updateFirestore(userData);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        binding.editGambar.setEnabled(true);
                        binding.btnSimpan.setEnabled(true);
                        isUploadingImage = false;
                    }
                })
                .dispatch();
    }

    private void updateFirestore(Map<String, Object> userData) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .update(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("ProfileSave", "Gagal memperbarui profil: " + e.getMessage());
                    binding.editGambar.setEnabled(true);
                    binding.btnSimpan.setEnabled(true);
                    isUploadingImage = false;
                    Toast.makeText(this, "Gagal memperbarui profil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}