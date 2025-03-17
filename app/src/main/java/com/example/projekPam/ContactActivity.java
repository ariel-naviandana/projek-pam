package com.example.projekPam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projekPam.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCall.setOnClickListener(this);
        binding.btnEmail.setOnClickListener(this);
        binding.btnWebsite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnCall) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:089617349913"));
            startActivity(intent);
        } else if (v == binding.btnEmail) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"arielnaviandanaputra@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Customer Inquiry");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, I need more information...");
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else if (v == binding.btnWebsite) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://filkom.ub.ac.id"));
            startActivity(intent);
        }
    }
}
