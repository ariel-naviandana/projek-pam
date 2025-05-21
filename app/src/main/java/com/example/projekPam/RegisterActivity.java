package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        binding.btnRegister.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRegister) {
            handleRegister();
        } else if (id == R.id.txtLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void handleRegister() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        String fullname = binding.etUsername.getText().toString().trim(); // Assuming username is used as fullname

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Prepare user data for Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("id", user.getUid());
                            userData.put("email", email);
                            userData.put("fullname", fullname);
                            userData.put("username", username);
                            userData.put("image", "");
                            userData.put("xp", 0);
                            userData.put("coin", 0);
                            userData.put("created_at", new Date());
                            userData.put("role", "user");

                            // Save to Firestore
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        // Send email verification
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(verificationTask -> {
                                                    if (verificationTask.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this,
                                                                "Registrasi Berhasil! Silakan verifikasi email Anda.",
                                                                Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        intent.putExtra("USERNAME", email);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this,
                                                                "Gagal mengirim email verifikasi: " + verificationTask.getException().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this,
                                                "Gagal menyimpan data pengguna: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, "Email sudah terdaftar!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registrasi Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}