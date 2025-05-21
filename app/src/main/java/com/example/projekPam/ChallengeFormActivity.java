package com.example.projekPam;

import android.app.DatePickerDialog;
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
import com.example.projekPam.databinding.ActivityChallengeFormBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class ChallengeFormActivity extends AppCompatActivity {
    private ActivityChallengeFormBinding binding;
    private FirebaseFirestore db;
    private String challengeId;
    private final Calendar calendar = Calendar.getInstance();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl; // Retain existing image URL
    private boolean isCloudinaryInitialized = false;
    private boolean isUploadingImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        challengeId = getIntent().getStringExtra("CHALLENGE_ID");

        initCloudinary();
        setupForm();
        setupClickListeners();
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
        if (challengeId != null) {
            // Edit mode
            binding.tvChallengeTitle.setText("Edit Challenge");
            binding.etJudulChallenge.setText(getIntent().getStringExtra("JUDUL"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            // Tanggal mulai
            long startMillis = getIntent().getLongExtra("DATE_START", 0);
            if (startMillis > 0) {
                binding.etMulai.setText(sdf.format(new Date(startMillis)));
            }

            // Tanggal selesai
            long endMillis = getIntent().getLongExtra("DATE_END", 0);
            if (endMillis > 0) {
                binding.etSelesai.setText(sdf.format(new Date(endMillis)));
            }

            // Load and preserve existing image
            imageUrl = getIntent().getStringExtra("IMAGE"); // Initialize with existing URL
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(binding.ivChallengeImage);
            } else {
                binding.ivChallengeImage.setImageResource(R.drawable.challenge_image);
            }
        } else {
            // New challenge mode
            binding.tvChallengeTitle.setText("Tambah Challenge");
            binding.ivChallengeImage.setImageResource(R.drawable.challenge_image);
            imageUrl = null; // No existing image for new challenges
        }
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.ivChallengeImage.setOnClickListener(v -> openImagePicker());

        binding.btnSaveChallenge.setOnClickListener(v -> saveChallenge());

        // Date pickers
        binding.etMulai.setOnClickListener(v -> showDatePickerDialog(true));
        binding.etSelesai.setOnClickListener(v -> showDatePickerDialog(false));
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
            Glide.with(this).load(imageUri).into(binding.ivChallengeImage);
        }
    }

    private void showDatePickerDialog(boolean isStartDate) {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(calendar.getTime());

                    if (isStartDate) {
                        binding.etMulai.setText(date);
                    } else {
                        binding.etSelesai.setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveChallenge() {
        // Check if an image upload is in progress
        if (isUploadingImage) {
            Toast.makeText(this, "Harap tunggu hingga gambar selesai diunggah.", Toast.LENGTH_SHORT).show();
            return;
        }

        String judul = binding.etJudulChallenge.getText().toString().trim();
        String startDateStr = binding.etMulai.getText().toString().trim();
        String endDateStr = binding.etSelesai.getText().toString().trim();

        if (judul.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (endDate.before(startDate)) {
                Toast.makeText(this, "Tanggal selesai harus setelah tanggal mulai", Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare challenge data
            Map<String, Object> challenge = new HashMap<>();
            challenge.put("judul", judul);
            challenge.put("date_start", new Timestamp(startDate));
            challenge.put("date_end", new Timestamp(endDate));
            challenge.put("created_at", FieldValue.serverTimestamp());

            // Include existing image URL if available, only update if new image is uploaded
            if (imageUrl != null && !imageUrl.isEmpty() && imageUri == null) {
                challenge.put("image", imageUrl); // Use existing image if no new one selected
            }

            // Handle image upload if a new image is selected
            if (imageUri != null) {
                uploadImageToCloudinary(challenge);
            } else {
                saveToFirestore(challenge);
            }
        } catch (Exception e) {
            Log.e("DateError", "Error parsing date", e);
            Toast.makeText(this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToCloudinary(Map<String, Object> challenge) {
        binding.btnSaveChallenge.setEnabled(false);
        binding.ivChallengeImage.setEnabled(false);
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
                        imageUrl = (String) resultData.get("secure_url"); // Update imageUrl with new URL
                        challenge.put("image", imageUrl);
                        saveToFirestore(challenge);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        binding.btnSaveChallenge.setEnabled(true);
                        binding.ivChallengeImage.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(ChallengeFormActivity.this, "Gagal mengunggah gambar: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        // Use existing image or proceed without image
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            challenge.put("image", imageUrl);
                        }
                        saveToFirestore(challenge);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        binding.btnSaveChallenge.setEnabled(true);
                        binding.ivChallengeImage.setEnabled(true);
                        isUploadingImage = false;
                    }
                })
                .dispatch();
    }

    private void saveToFirestore(Map<String, Object> challenge) {
        String collectionName = "challenge";

        if (challengeId == null) {
            db.collection(collectionName)
                    .add(challenge)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Challenge berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error adding document", e);
                        binding.btnSaveChallenge.setEnabled(true);
                        binding.ivChallengeImage.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(this, "Gagal menambahkan challenge", Toast.LENGTH_SHORT).show();
                    });
        } else {
            db.collection(collectionName)
                    .document(challengeId)
                    .set(challenge)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Challenge berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error updating document", e);
                        binding.btnSaveChallenge.setEnabled(true);
                        binding.ivChallengeImage.setEnabled(true);
                        isUploadingImage = false;
                        Toast.makeText(this, "Gagal memperbarui challenge", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cleanup to avoid memory leaks
        binding = null;
    }
}