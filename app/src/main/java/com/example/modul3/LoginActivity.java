package com.example.modul3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.modul3.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(this);
        binding.txtForgotPassword.setOnClickListener(this);
        binding.txtRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin) {
            handleLogin();
        } else if (id == R.id.txtForgotPassword) {
            Toast.makeText(this, "Fitur Lupa Password belum Tersedia", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.txtRegister) {
            Toast.makeText(this, "Fitur Register belum tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        } else {
            if (isValidEmail(email) && isValidPassword(password)) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email atau Password tidak valid!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}