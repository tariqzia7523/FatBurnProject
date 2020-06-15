package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VeneuActivity extends AppCompatActivity {
    DatabaseReference myRef;
    ArrayList<String> venuesNames;
    ArrayList<VeneuModel> classTimings;
    Spinner spinner;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veneu);
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(VeneuActivity.this);
        progressDialog.setMessage("Please Wait...");
        TAG="***veneu";
        spinner=findViewById(R.id.spinner);
        venuesNames=new ArrayList<>();
        classTimings=new ArrayList<>();
        recyclerView=findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VeneuActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),VeneuActivity.this,classTimings);
        recyclerView.setAdapter(myAdapter);
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("VeneuClass");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        venuesNames.add(dataSnapshot1.getKey());
                    }
                    ArrayAdapter<String> adapter =
//                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, venuesNames);
                            new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, venuesNames);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    for(DataSnapshot dataSnapshot1: dataSnapshot.child(venuesNames.get(0)).getChildren()){
                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.child("TimeTable").getChildren()){
                            Log.e(TAG,dataSnapshot2.getKey()+" is id");
                            VeneuModel veneuModel=new VeneuModel();
                            veneuModel.setVenue(venuesNames.get(0));
                            veneuModel.setClassid(dataSnapshot1.getKey());
                            veneuModel.setDay(dataSnapshot2.getKey());
                            veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                            classTimings.add(veneuModel);
                        }
                    }


                    progressDialog.dismiss();
                    myAdapter.notifyDataSetChanged();

                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String selectedItemText = (String) parentView.getItemAtPosition(position);
                progressDialog.show();
                myRef.child(selectedItemText).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        classTimings.clear();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for(DataSnapshot dataSnapshot2 : dataSnapshot1.child("TimeTable").getChildren()){
                                Log.e(TAG,dataSnapshot2.getKey()+" is id");
                                VeneuModel veneuModel=new VeneuModel();
                                veneuModel.setClassid(dataSnapshot1.getKey());
                                veneuModel.setVenue(selectedItemText);
                                veneuModel.setDay(dataSnapshot2.getKey());
                                veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                                classTimings.add(veneuModel);
                            }
                        }
                        progressDialog.dismiss();
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<VeneuModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            ImageView image;
            TextView name,day;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                image=view.findViewById(R.id.select_image);
                image.setVisibility(View.GONE);
                name=view.findViewById(R.id.name);
//                day=view.findViewById(R.id.day);
//                day.setVisibility(View.GONE);


            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<VeneuModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.revenue_class_item, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText(data.get(position).getDay()+" Time "+data.get(position).getTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity,VeneClassDetailActivity.class).putExtra("data",data.get(position)));
                }
            });


        }
        @Override
        public int getItemCount() {
//        return  5;
            return data.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }




}
