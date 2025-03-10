package com.example.projekPam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projekPam.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLogin)
            cekLogin();
    }

    private void cekLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty())
            Toast.makeText(this, "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        else {
            if (email.equals("arielnaviandanaputra@gmail.com") && password.equals("235150701111010")) {
                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HeadActivity.class);
                startActivity(intent);
                finish();
            } else
                Toast.makeText(this, "Email atau Password tidak valid!", Toast.LENGTH_SHORT).show();
        }
    }
}