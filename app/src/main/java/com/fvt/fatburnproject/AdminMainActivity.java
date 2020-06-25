package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {

    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getSupportActionBar().hide();
        TAG="***Admin";
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(AdminMainActivity.this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        findViewById(R.id.add_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,AdminAddClassActivity.class));
                startActivity(new Intent(AdminMainActivity.this,AdminAddNewActivity.class));
            }
        });
        findViewById(R.id.add_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,AdminAddClassActivity.class));
                startActivity(new Intent(AdminMainActivity.this,AdminAddAboutActivity.class));
            }
        });
        findViewById(R.id.add_faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,AdminAddClassActivity.class));
                startActivity(new Intent(AdminMainActivity.this,AdminFAQActivity.class));
            }
        });


    }



}
