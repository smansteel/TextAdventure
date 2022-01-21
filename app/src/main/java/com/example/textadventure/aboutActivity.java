package com.example.textadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class aboutActivity extends AppCompatActivity {
    Button btnGoBackAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SetupControlsAbout();
    }
    protected void SetupControlsAbout()
    {
        btnGoBackAbout = findViewById(R.id.btnGoBackAbout);
        btnGoBackAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });}
}