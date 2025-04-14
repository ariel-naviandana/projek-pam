package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projekPam.databinding.ActivityMateriBinding;

import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMateriBinding binding;
    private MateriAdapter materiAdapter;
    private List<Materi> listMateri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Binding clickable object
        binding.btnBackMateri.setOnClickListener(this);
        binding.fabTambahMateri.setOnClickListener(this);

        // Set judul dari intent
        String title = getIntent().getStringExtra("TITLE");
        binding.tvCardJudulMateri.setText(title);

        // Setup RecyclerView
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        // Data local
        listMateri = new ArrayList<>();
        listMateri.add(new Materi("Gas Rumah Kaca dan Dampaknya", R.drawable.fire_icon));
        listMateri.add(new Materi("Bukti Perubahan Iklim pada Ekosistem", R.drawable.ic_earth));
        listMateri.add(new Materi("Energi Terbarukan dan Perubahan Iklim", R.drawable.ic_electricity));
        listMateri.add(new Materi("Adaptasi dan Mitigasi Perubahan Iklim", R.drawable.ic_umbrella));
        listMateri.add(new Materi("Perubahan Iklim dan Pertanian", R.drawable.ic_ecofriendly));

        // Buat adapter dan pasang ke RecyclerView
        materiAdapter = new MateriAdapter(listMateri);
        binding.rvDaftarMateri.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDaftarMateri.setAdapter(materiAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBackMateri) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.fabTambahMateri) {
            Intent intent = new Intent(this, AddMateriActivity.class);
            startActivity(intent);
            finish();
        }
    }
}