package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminEditPayActivty extends AppCompatActivity {

    DatabaseReference myRef;
    EditText fbx,wbx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_pay_activty);
        getSupportActionBar().hide();
        fbx=findViewById(R.id.fbx_price);
        fbx.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        wbx=findViewById(R.id.wbx_price);
        wbx.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("Prices");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    String fb=dataSnapshot.child("FBX").child("price").getValue(String.class);
                    fb=fb.substring(1,fb.length());
                    fbx.setText(fb);
                    fb=dataSnapshot.child("WBX").child("price").getValue(String.class);
                    fb=fb.substring(1,fb.length());
                    wbx.setText(fb);
                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.add_price).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fbx.getText().toString().isEmpty() || wbx.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please add both prices",Toast.LENGTH_LONG).show();
                    return;
                }

                myRef.child("FBX").child("price").setValue("£"+fbx.getText().toString());
                myRef.child("WBX").child("price").setValue("£"+wbx.getText().toString());
                Toast.makeText(getApplicationContext(),"Amount updated",Toast.LENGTH_LONG).show();
            }
        });

    }
}
