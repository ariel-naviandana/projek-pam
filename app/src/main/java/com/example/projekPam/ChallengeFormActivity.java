package com.example.projekPam;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityChallengeFormBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChallengeFormActivity extends AppCompatActivity {
    private ActivityChallengeFormBinding binding;
    private FirebaseFirestore db;
    private String challengeId;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChallengeFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        challengeId = getIntent().getStringExtra("CHALLENGE_ID");

        setupForm();
        setupClickListeners();
    }

    private void setupForm() {
        if (challengeId != null) {
            // Edit mode
            binding.tvChallengeTitle.setText("Edit Challenge");
            binding.etJudulChallenge.setText(getIntent().getStringExtra("JUDUL"));
            // Set tanggal mulai dan selesai
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

            // Set default image (from drawable)
            binding.ivChallengeImage.setImageResource(R.drawable.challenge_image);
        }
    }

    private void setupClickListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.ivChallengeImage.setOnClickListener(v -> {
            // Tetap menggunakan drawable image
            binding.ivChallengeImage.setImageResource(R.drawable.challenge_image);
            Toast.makeText(this, "Using default challenge image", Toast.LENGTH_SHORT).show();
        });

        binding.btnSaveChallenge.setOnClickListener(v -> saveChallenge());

        // Date pickers
        binding.etMulai.setOnClickListener(v -> showDatePickerDialog(true));
        binding.etSelesai.setOnClickListener(v -> showDatePickerDialog(false));
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

            // Konversi ke Firestore Timestamp
            Timestamp startTimestamp = new Timestamp(startDate);
            Timestamp endTimestamp = new Timestamp(endDate);

            Map<String, Object> challenge = new HashMap<>();
            challenge.put("judul", judul);
            challenge.put("date_start", startTimestamp);
            challenge.put("date_end", endTimestamp);
            challenge.put("created_at", FieldValue.serverTimestamp());
//          challenge.put("image", R.drawable.challenge_image);

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
                            Toast.makeText(this, "Gagal menambahkan challenge", Toast.LENGTH_SHORT).show();
                        });
            } else {
                db.collection(collectionName)
                        .document(challengeId)
                        .set(challenge) // Gunakan set() untuk update semua field
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Challenge berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirestoreError", "Error updating document", e);
                            Toast.makeText(this, "Gagal memperbarui challenge", Toast.LENGTH_SHORT).show();
                        });
            }
        } catch (Exception e) {
            Log.e("DateError", "Error parsing date", e);
            Toast.makeText(this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
        }
    }
}