package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnRegister.setOnClickListener(this);
        binding.txtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRegister)
            handleRegister();
        else if (id == R.id.txtLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void handleRegister() {
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
        else if (!isValidEmail(email))
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
        else if (password.length() < 6)
            Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            finish();
        }
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}