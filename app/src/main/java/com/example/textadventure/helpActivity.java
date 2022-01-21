package com.example.textadventure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class helpActivity extends AppCompatActivity {
    Button btnGoToBackHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        SetupControlsHelp();
    }
    protected void SetupControlsHelp()
    {
        btnGoToBackHelp = findViewById(R.id.btnGoToBackHelp);
        btnGoToBackHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });}


}