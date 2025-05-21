package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityMateriBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMateriBinding binding;
    private MateriAdapter materiAdapter;
    private List<Materi> listMateri;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Binding clickable objects
        binding.btnBackMateri.setOnClickListener(this);
        binding.fabTambahMateri.setOnClickListener(this);
        binding.btSearchMateri.setOnClickListener(this);

        // Handle intent extras
        String materiId = getIntent().getStringExtra("MATERI_ID");
        String judul = getIntent().getStringExtra("JUDUL");
        if (judul != null && !judul.isEmpty()) {
            binding.tvCardJudulMateri.setText(judul);
        } else {
            binding.tvCardJudulMateri.setText("Daftar Materi");
        }

        // Setup RecyclerView
        setupRecyclerView();

        // Load data from Firestore
        loadMateriData();
    }

    private void setupRecyclerView() {
        listMateri = new ArrayList<>();
        materiAdapter = new MateriAdapter(this, listMateri);
        binding.rvDaftarMateri.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDaftarMateri.setAdapter(materiAdapter);

        // Set item click listener
        materiAdapter.setOnItemClickListener(materi ->
                Toast.makeText(MateriActivity.this, materi.getJudul(), Toast.LENGTH_SHORT).show()
        );
    }

    private void loadMateriData() {
        db.collection("materi")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    listMateri.clear();
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot) {
                        String id = doc.getId();
                        String judul = doc.getString("judul");
                        String image = doc.getString("image");
                        String deskripsi = doc.getString("deskripsi");
                        com.google.firebase.Timestamp createdAt = doc.getTimestamp("created_at");

                        // Validate required fields
                        if (judul == null || judul.isEmpty() || createdAt == null) {
                            Log.w("MateriActivity", "Skipping materi document with missing required fields: " + id);
                            continue;
                        }

                        Materi materi = new Materi(id, judul, image, deskripsi, createdAt);
                        listMateri.add(materi);
                    }
                    materiAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("MateriActivity", "Failed to load materi: " + e.getMessage());
                    Toast.makeText(this, "Gagal memuat materi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
        } else if (id == R.id.btSearchMateri) {
            Toast.makeText(this, "Fitur pencarian belum diimplementasikan", Toast.LENGTH_SHORT).show();
        }
    }
}