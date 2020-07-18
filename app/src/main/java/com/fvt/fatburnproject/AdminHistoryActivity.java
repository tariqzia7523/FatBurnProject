package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHistoryActivity extends AppCompatActivity {

    ArrayList<HistoryModel> historyModels;
    DatabaseReference myRefHistory;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();
        historyModels=new ArrayList<>();
        recyclerView=findViewById(R.id.list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminHistoryActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),AdminHistoryActivity.this,historyModels);
        recyclerView.setAdapter(myAdapter);
        myRefHistory = FirebaseDatabase.getInstance().getReference("Classes").child("History");
        myRefHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    historyModels.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        HistoryModel historyModel=dataSnapshot1.getValue(HistoryModel.class);
                        historyModel.setId(dataSnapshot1.getKey());
                        historyModels.add(historyModel);
                    }
                    myAdapter.notifyDataSetChanged();
                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<HistoryModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView name,day,time,phoneNumber,transectinid,paymentId;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                name=view.findViewById(R.id.name);
                day=view.findViewById(R.id.day);
                day.setVisibility(View.GONE);
                time=view.findViewById(R.id.time);
                time.setVisibility(View.GONE);
                phoneNumber=view.findViewById(R.id.phone_number);
//                phoneNumber.setVisibility(View.GONE);
                transectinid=view.findViewById(R.id.transection_id);
                paymentId=view.findViewById(R.id.payment_id);
                paymentId.setVisibility(View.GONE);
            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<HistoryModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.veneu_student_model, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText("Name : "+data.get(position).getName());
            holder.transectinid.setText("Transaction id : "+data.get(position).getTransectoionid());
            holder.phoneNumber.setText("Payment id : "+data.get(position).getPaymentId());


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
