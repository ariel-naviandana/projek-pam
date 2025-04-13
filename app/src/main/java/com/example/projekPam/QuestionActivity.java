package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityQuestionBinding binding;
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(this);
        binding.btnAddQuestion.setOnClickListener(this);

        ArrayList<Question> questionList = new ArrayList<>();
        questionList.add(new Question("Apa itu lingkungan?", "Pilihan Ganda", "A"));
        questionList.add(new Question("Apa warna langit?", "Pilihan Ganda", "Biru"));
        questionList.add(new Question("Apakah bumi bulat?", "Benar/Salah", "Benar"));

        questionAdapter = new QuestionAdapter(questionList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(questionAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnBack)
            finish();
        else if (id == R.id.btnAddQuestion) {
            Intent intent = new Intent(this, QuestionFormActivity.class);
            startActivity(intent);
        }
    }
}