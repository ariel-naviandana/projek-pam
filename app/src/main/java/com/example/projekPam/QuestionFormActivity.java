package com.example.projekPam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.projekPam.databinding.ActivityQuestionFormBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionFormActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuestionFormBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl;
    private String quizId;
    private String questionId;
    private boolean isCloudinaryInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCloudinary();
        setupQuestionTypeSpinner();

        binding.btnBack.setOnClickListener(this);
        binding.btnSaveQuestion.setOnClickListener(this);
        binding.btnUploadImage.setOnClickListener(this);

        quizId = getIntent().getStringExtra("QUIZ_ID");
        questionId = getIntent().getStringExtra("QUESTION_ID");

        if (questionId != null) {
            binding.title.setText("Edit Soal");
            loadQuestionData();
        }
    }

    private void initCloudinary() {
        if (!isCloudinaryInitialized) {
            try {
                Map<String, String> config = new HashMap<>();
                config.put("cloud_name", "dto6d9tbe");
                MediaManager.init(this, config);
                isCloudinaryInitialized = true;
            } catch (IllegalStateException e) {
                Log.e("Cloudinary", "MediaManager already initialized");
            }
        }
    }

    private void setupQuestionTypeSpinner() {
        String[] questionTypes = {"Pilihan Ganda", "Benar/Salah", "Isian Singkat"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, questionTypes);
        binding.spinnerQuestionType.setAdapter(adapter);

        binding.spinnerQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.layoutOptions.setVisibility(View.GONE);
                binding.layoutTrueFalse.setVisibility(View.GONE);
                binding.layoutShortAnswer.setVisibility(View.GONE);

                switch (position) {
                    case 0:
                        binding.layoutOptions.setVisibility(View.VISIBLE);
                        setupCorrectAnswerDropdown(new String[]{"A", "B", "C", "D"});
                        break;
                    case 1:
                        binding.layoutTrueFalse.setVisibility(View.VISIBLE);
                        setupCorrectAnswerDropdown(new String[]{"Benar", "Salah"});
                        break;
                    case 2:
                        binding.layoutShortAnswer.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupCorrectAnswerDropdown(String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        if (binding.layoutOptions.getVisibility() == View.VISIBLE) {
            binding.spinnerCorrectAnswer.setAdapter(adapter);
        } else if (binding.layoutTrueFalse.getVisibility() == View.VISIBLE) {
            binding.spinnerTrueFalseAnswer.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnSaveQuestion) {
            saveQuestion();
        } else if (id == R.id.btnUploadImage) {
            openImagePicker();
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

            Glide.with(this)
                    .load(imageUri)
                    .into(binding.ivPreviewImage);

            uploadImageToCloudinary(imageUri);
        }
    }

    private void uploadImageToCloudinary(Uri imageUri) {
        binding.btnUploadImage.setEnabled(false);
        binding.btnUploadImage.setText("Mengunggah...");

        MediaManager.get().upload(imageUri)
                .unsigned("ecokids")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d("Cloudinary", "Mulai mengupload gambar");
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get("secure_url");
                        binding.btnUploadImage.setEnabled(true);
                        binding.btnUploadImage.setText("Unggah Gambar");
                        Toast.makeText(QuestionFormActivity.this,
                                "Gambar berhasil diunggah", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        binding.btnUploadImage.setEnabled(true);
                        binding.btnUploadImage.setText("Unggah Gambar");
                        Toast.makeText(QuestionFormActivity.this,
                                "Gagal mengunggah gambar: " + error.getDescription(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        binding.btnUploadImage.setEnabled(true);
                        binding.btnUploadImage.setText("Unggah Gambar");
                    }
                })
                .dispatch();
    }

    private void saveQuestion() {
        String question = binding.etQuestion.getText().toString().trim();
        String questionType = binding.spinnerQuestionType.getSelectedItem().toString();

        if (question.isEmpty()) {
            Toast.makeText(this, "Pertanyaan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> questionData = new HashMap<>();
        questionData.put("pertanyaan", question);
        questionData.put("tipe", questionType);
        questionData.put("created_at", Timestamp.now());

        if (imageUrl != null && !imageUrl.isEmpty()) {
            questionData.put("gambar", imageUrl);
        }

        switch (questionType) {
            case "Pilihan Ganda":
                List<String> pilihan = Arrays.asList(
                        binding.etOptionA.getText().toString().trim(),
                        binding.etOptionB.getText().toString().trim(),
                        binding.etOptionC.getText().toString().trim(),
                        binding.etOptionD.getText().toString().trim()
                );

                for (String option : pilihan) {
                    if (option.isEmpty()) {
                        Toast.makeText(this, "Semua opsi jawaban harus diisi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                questionData.put("pilihan", pilihan);
                questionData.put("jawaban", binding.spinnerCorrectAnswer.getSelectedItem().toString());
                break;

            case "Benar/Salah":
                questionData.put("jawaban", binding.spinnerTrueFalseAnswer.getSelectedItem().toString());
                break;

            case "Isian Singkat":
                String shortAnswer = binding.etShortAnswer.getText().toString().trim();
                if (shortAnswer.isEmpty()) {
                    Toast.makeText(this, "Jawaban isian singkat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                questionData.put("jawaban", shortAnswer);
                break;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (questionId != null) {
            db.collection("kuis").document(quizId).collection("soal").document(questionId)
                    .set(questionData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Soal berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal memperbarui soal", Toast.LENGTH_SHORT).show()
                    );
        } else {
            db.collection("kuis").document(quizId).collection("soal")
                    .add(questionData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Soal berhasil disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal menyimpan soal", Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void loadQuestionData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("kuis").document(quizId).collection("soal").document(questionId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        binding.etQuestion.setText(document.getString("pertanyaan"));
                        String questionType = document.getString("tipe");
                        binding.spinnerQuestionType.setSelection(getTypeIndex(questionType));

                        String savedImageUrl = document.getString("gambar");
                        if (savedImageUrl != null && !savedImageUrl.isEmpty()) {
                            imageUrl = savedImageUrl;
                            Glide.with(this)
                                    .load(savedImageUrl)
                                    .into(binding.ivPreviewImage);
                        }

                        switch (questionType) {
                            case "Pilihan Ganda":
                                ArrayList<String> pilihan = (ArrayList<String>) document.get("pilihan");
                                if (pilihan != null && pilihan.size() >= 4) {
                                    binding.etOptionA.setText(pilihan.get(0));
                                    binding.etOptionB.setText(pilihan.get(1));
                                    binding.etOptionC.setText(pilihan.get(2));
                                    binding.etOptionD.setText(pilihan.get(3));
                                }
                                binding.spinnerCorrectAnswer.setSelection(getAnswerIndex(document.getString("jawaban"), new String[]{"A", "B", "C", "D"}));
                                break;
                            case "Benar/Salah":
                                binding.spinnerTrueFalseAnswer.setSelection(getAnswerIndex(document.getString("jawaban"), new String[]{"Benar", "Salah"}));
                                break;
                            case "Isian Singkat":
                                binding.etShortAnswer.setText(document.getString("jawaban"));
                                break;
                        }
                    }
                });
    }

    private int getTypeIndex(String type) {
        switch (type) {
            case "Pilihan Ganda":
                return 0;
            case "Benar/Salah":
                return 1;
            case "Isian Singkat":
                return 2;
            default:
                return 0;
        }
    }

    private int getAnswerIndex(String answer, String[] options) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(answer)) {
                return i;
            }
        }
        return 0;
    }
}