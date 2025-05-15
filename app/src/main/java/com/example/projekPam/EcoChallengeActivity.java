package com.example.projekPam;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityEcochallengeJoinBinding;
import java.util.ArrayList;

public class EcoChallengeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityEcochallengeJoinBinding binding;
    private ProgressAdapter progressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEcochallengeJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String title = getIntent().getStringExtra("TITLE");
        if (title != null)
            binding.tvChallengeTitle.setText(title);

        binding.btnBack.setOnClickListener(this);

        ArrayList<ProgressItem> progressItemList = new ArrayList<>();
        progressItemList.add(new ProgressItem("Bawa Botol Minum", R.drawable.ic_bottle, 0));
        progressItemList.add(new ProgressItem("Bawa Bekal Makanan", R.drawable.ic_bekal, 0));

        binding.recyclerViewEcoChallenge.setLayoutManager(new LinearLayoutManager(this));
        progressAdapter = new ProgressAdapter(progressItemList);
        binding.recyclerViewEcoChallenge.setAdapter(progressAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack)
            finish();
    }
}