package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekPam.databinding.ActivityMateriBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMateriBinding binding;
    private MateriAdapter materiAdapter;
    private List<Materi> listMateri;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userRole = "user"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMateriBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
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

        // Check user role and load data
        loadUserRole();
    }

    private void setupRecyclerView() {
        listMateri = new ArrayList<>();
        materiAdapter = new MateriAdapter(this, listMateri, userRole);
        binding.rvDaftarMateri.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDaftarMateri.setAdapter(materiAdapter);

        // Set item click listener for edit
        materiAdapter.setOnItemClickListener(materi -> {
            if (userRole.equals("admin")) {
                Intent intent = new Intent(MateriActivity.this, AddMateriActivity.class);
                intent.putExtra("MATERI_ID", materi.getId());
                intent.putExtra("JUDUL", materi.getJudul());
                intent.putExtra("DESKRIPSI", materi.getDeskripsi());
                intent.putExtra("IMAGE", materi.getImage());
                startActivity(intent);
            } else {
                Toast.makeText(MateriActivity.this, "Lihat: " + materi.getJudul(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserRole() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak ditemukan. Silakan login kembali.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userRole = documentSnapshot.getString("role") != null ? documentSnapshot.getString("role") : "user";
                        // Update UI based on role
                        binding.fabTambahMateri.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
                        binding.NavbarAdmin.setVisibility(userRole.equals("admin") ? View.VISIBLE : View.GONE);
                        materiAdapter.setUserRole(userRole);
                        materiAdapter.notifyDataSetChanged();
                        // Setup navbar after role is determined
                        setupNavbar();
                        // Load data after role is confirmed
                        loadMateriData();
                    } else {
                        userRole = "user";
                        binding.fabTambahMateri.setVisibility(View.GONE);
                        binding.NavbarAdmin.setVisibility(View.GONE);
                        setupNavbar();
                        loadMateriData();
                        Toast.makeText(this, "Data pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Gagal mengambil role pengguna: " + e.getMessage());
                    userRole = "user";
                    binding.fabTambahMateri.setVisibility(View.GONE);
                    binding.NavbarAdmin.setVisibility(View.GONE);
                    setupNavbar();
                    loadMateriData();
                    Toast.makeText(this, "Gagal mengambil data pengguna.", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupNavbar() {
        if (userRole.equals("admin")) {
            binding.homeAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.materi.setOnClickListener(v -> {
                Toast.makeText(this, "Anda sudah di halaman Materi.", Toast.LENGTH_SHORT).show();
            });

            binding.quiz.setOnClickListener(v -> {
                Intent intent = new Intent(this, QuizActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.challenge.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChallengeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            });

            binding.profileAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileUtamaActivity.class);
                db.collection("users").document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            String username = documentSnapshot.getString("username");
                            intent.putExtra("USERNAME", username != null ? username : "User");
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        });
            });
        }
    }

    private void loadMateriData() {
        db.collection("materi")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("MateriActivity", "Failed to load materi: " + error.getMessage());
                        Toast.makeText(this, "Gagal memuat materi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (querySnapshot != null) {
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
                    }
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
        } else if (id == R.id.btSearchMateri) {
            Toast.makeText(this, "Fitur pencarian belum diimplementasikan", Toast.LENGTH_SHORT).show();
        }
    }
}