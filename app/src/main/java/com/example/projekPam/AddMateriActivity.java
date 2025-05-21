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
import com.example.projekPam.databinding.ActivityAddMateriBinding;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddMateriActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityAddMateriBinding binding;
    private FirebaseFirestore db;
    private String materiId;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl;
    private boolean isCloudinaryInitialized = false;
    private boolean isUploadingImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        materiId = getIntent().getStringExtra("MATERI_ID");

        initCloudinary();
        setupForm();
        binding.btnSubmitMateri.setOnClickListener(this);
        binding.imgPreviewMateri.setOnClickListener(v -> openImagePicker());
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

    private void setupForm() {
        if (materiId != null) {
            // Edit mode
            binding.btnSubmitMateri.setText("Update");
            String judul = getIntent().getStringExtra("JUDUL");
            String deskripsi = getIntent().getStringExtra("DESKRIPSI");
            imageUrl = getIntent().getStringExtra("IMAGE");

            binding.etJudulMateri.setText(judul);
            binding.etDetailMateri.setText(deskripsi);

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(binding.imgPreviewMateri);
            } else {
                binding.imgPreviewMateri.setImageResource(R.drawable.ic_image);
            }
        } else {
            // Add mode
            binding.btnSubmitMateri.setText("Submit");
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
            Glide.with(this).load(imageUri).into(binding.imgPreviewMateri);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmitMateri) {
            saveMateri();
        }
    }

    private void saveMateri() {
        if (isUploadingImage) {
            Toast.makeText(this, "Harap tunggu hingga gambar selesai diunggah.", Toast.LENGTH_SHORT).show();
            return;
        }

        String judul = binding.etJudulMateri.getText().toString().trim();
        String deskripsi = binding.etDetailMateri.getText().toString().trim();

        if (judul.isEmpty()) {
            binding.etJudulMateri.setError("Judul tidak boleh kosong");
            return;
        }
        if (deskripsi.isEmpty()) {
            binding.etDetailMateri.setError("Detail tidak boleh kosong");
            return;
        }

        Map<String, Object> materi = new HashMap<>();
        materi.put("judul", judul);
        materi.put("deskripsi", deskripsi);
        materi.put("created_at", FieldValue.serverTimestamp());

        // Include existing image URL if available
        if (imageUrl != null && !imageUrl.isEmpty() && imageUri == null) {
            materi.put("image", imageUrl);
        }

        // Handle image upload if selected
        if (imageUri != null) {
            uploadImageToCloudinary(materi);
        } else {
            saveToFirestore(materi);
        }
    }

    private void uploadImageToCloudinary(Map<String, Object> materi) {
        binding.btnSubmitMateri.setEnabled(false);
        binding.imgPreviewMateri.setEnabled(false);
        isUploadingImage = true;

        MediaManager.get().upload(imageUri)
                .unsigned("ecokids") // Replace with your Cloudinary preset
                .option("resource_type", "image")
                .option("quality", "80")
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
                        materi.put("image", imageUrl);
                        saveToFirestore(materi);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        binding.btnSubmitMateri.setEnabled(true);
                        binding.imgPreviewMateri.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(AddMateriActivity.this, "Gagal mengunggah gambar: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        saveToFirestore(materi); // Save without image on error
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        binding.btnSubmitMateri.setEnabled(true);
                        binding.imgPreviewMateri.setEnabled(true);
                        isUploadingImage = false;
                    }
                })
                .dispatch();
    }

    private void saveToFirestore(Map<String, Object> materi) {
        if (materiId == null) {
            // Create new materi
            db.collection("materi")
                    .add(materi)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Materi berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MateriActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error adding materi", e);
                        binding.btnSubmitMateri.setEnabled(true);
                        binding.imgPreviewMateri.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(this, "Gagal menambahkan materi", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Update existing materi
            db.collection("materi")
                    .document(materiId)
                    .set(materi)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Materi berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MateriActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error updating materi", e);
                        binding.btnSubmitMateri.setEnabled(true);
                        binding.imgPreviewMateri.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(this, "Gagal memperbarui materi", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}