package com.example.moviesdemo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviesdemo.R;

public class DetalleActivity extends AppCompatActivity {
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        bundle = getIntent().getExtras();
    }
}
