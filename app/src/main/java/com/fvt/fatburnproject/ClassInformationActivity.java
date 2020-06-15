package com.fvt.fatburnproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ClassInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_information);
        getSupportActionBar().hide();
    }
}
