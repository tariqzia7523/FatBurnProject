package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminMainActivity extends AppCompatActivity {

    String TAG;
    DatabaseReference myRefDiscount;
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
        myRefDiscount = FirebaseDatabase.getInstance().getReference("Classes").child("Discount");
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
        findViewById(R.id.edit_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,AdminAddClassActivity.class));
                startActivity(new Intent(AdminMainActivity.this,AdminEditPayActivty.class));
            }
        });//history_log
        findViewById(R.id.history_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AdminMainActivity.this,AdminAddClassActivity.class));
                startActivity(new Intent(AdminMainActivity.this,AdminHistoryActivity.class));
            }
        });
        findViewById(R.id.discount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountpopUp();
            }
        });


    }

    public void discountpopUp(){
            Log.e(TAG,"payment called");
//        Context c = getApplicationContext();
//        LinearLayout layout = new LinearLayout(c);
//        layout.setBackgroundColor(getResources().getColor(R.color.transbackground));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            AlertDialog.Builder al = new AlertDialog.Builder(AdminMainActivity.this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view;
//        view = inflater.inflate(R.layout.payment_layout, null);
            view = inflater.inflate(R.layout.discount_pop_up, null);
            al.setView(view);
            final AlertDialog value = al.create();
            value.setCancelable(false);
            //final ListView lv=new ListView(this);
             CheckBox checkBox=view.findViewById(R.id.check_box);
            final EditText discount = view.findViewById(R.id.discount);
            Button payButton = view.findViewById(R.id.update);
            myRefDiscount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        checkBox.setChecked(dataSnapshot.child("discountCheck").getValue(Boolean.class));
                    }catch (Exception c){
                        c.printStackTrace();
                    }
                    try{
                        discount.setText(dataSnapshot.child("discount").getValue(String.class).substring(1));
                    }catch (Exception c){
                        c.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( discount.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Please add all data",Toast.LENGTH_LONG).show();
                        return;
                    }

                    Map<String, Object> updates = new HashMap<String,Object>();
                    updates.put("discountCheck", checkBox.isChecked());
                    updates.put("discount", "%"+discount.getText().toString());
//etc

                    myRefDiscount.updateChildren(updates);
                    value.dismiss();
                    Toast.makeText(getApplicationContext(),"discount updated",Toast.LENGTH_LONG).show();
                }
            });

            value.show();
            value.setCancelable(false);

    }



}
