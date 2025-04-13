package com.example.projekPam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityQuestionFormBinding;

import java.io.IOException;

public class QuestionFormActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuestionFormBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupQuestionTypeSpinner();

        binding.btnBack.setOnClickListener(this);
        binding.btnSaveQuestion.setOnClickListener(this);
        binding.btnUploadImage.setOnClickListener(this);
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

                if (position == 0) {
                    binding.layoutOptions.setVisibility(View.VISIBLE);
                    setupCorrectAnswerDropdown(new String[]{"A", "B", "C", "D"});
                } else if (position == 1) {
                    binding.layoutTrueFalse.setVisibility(View.VISIBLE);
                    setupCorrectAnswerDropdown(new String[]{"Benar", "Salah"});
                } else if (position == 2) {
                    binding.layoutShortAnswer.setVisibility(View.VISIBLE);
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
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.ivPreviewImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveQuestion() {
        String question = binding.etQuestion.getText().toString().trim();
        String questionType = binding.spinnerQuestionType.getSelectedItem().toString();

        if (question.isEmpty()) {
            Toast.makeText(this, "Pertanyaan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionType.equals("Pilihan Ganda")) {
            String optionA = binding.etOptionA.getText().toString().trim();
            String optionB = binding.etOptionB.getText().toString().trim();
            String optionC = binding.etOptionC.getText().toString().trim();
            String optionD = binding.etOptionD.getText().toString().trim();

            if (optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty()) {
                Toast.makeText(this, "Semua opsi jawaban harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (questionType.equals("Isian Singkat")) {
            String shortAnswer = binding.etShortAnswer.getText().toString().trim();
            if (shortAnswer.isEmpty()) {
                Toast.makeText(this, "Jawaban isian singkat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Toast.makeText(this, "Soal berhasil disimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}