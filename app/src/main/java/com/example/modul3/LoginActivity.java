package com.example.modul3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    TextView txtTerms, txtForgotPassword, txtRegister;
    EditText etEmail, etPassword;
    Button btnLogin;
    ImageButton btnGmailLogin, btnFacebookLogin, btnTwitterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);

        ImageButton btnGmailLogin = findViewById(R.id.btnGmailLogin);
        ImageButton btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        ImageButton btnTwitterLogin = findViewById(R.id.btnTwitterLogin);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        txtTerms = findViewById(R.id.txtTerms);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtRegister = findViewById(R.id.txtRegister);

    }
}