package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

public class AdminOnlineClassDetailActivity extends AppCompatActivity {
    OnlineClassModel onlineClassModel;
    DatabaseReference myRefBooked;
    RecyclerView recyclerView;
    TextView title,timming,days,seats,seatsText;
//    ArrayList<String> list;
    ArrayList<PayUserModel> list;
    MyAdapter myAdapter;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_online_class_detail);
        getSupportActionBar().hide();
        TAG="***onlineDet";
        recyclerView=findViewById(R.id.student_list);
        list=new ArrayList<>();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminOnlineClassDetailActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),AdminOnlineClassDetailActivity.this,list);
        recyclerView.setAdapter(myAdapter);
        title=findViewById(R.id.tile);
        timming=findViewById(R.id.class_timing);
        days=findViewById(R.id.class_days);
        seats=findViewById(R.id.seats);
        seats.setVisibility(View.GONE);
        seatsText=findViewById(R.id.seats_text);
        seatsText.setVisibility(View.GONE);
        try{
            onlineClassModel=(OnlineClassModel)getIntent().getExtras().get("data");
            Log.e(TAG,"class id is "+onlineClassModel.getId());
            myRefBooked= FirebaseDatabase.getInstance().getReference().child("Classes").child("Booked").child("OnlineClasses").child(onlineClassModel.getId());
//            title.setText(onlineClassModel.getTitle());
            timming.setText(onlineClassModel.getTitle());
            days.setText(onlineClassModel.getPrice());
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            myRefBooked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        Log.e(TAG,"in onDataChange id is "+dataSnapshot.getKey());
                        Log.e(TAG,"in onDataChange id is count "+dataSnapshot.getChildrenCount());
                        list.clear();
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Log.e(TAG,"in loop id is "+dataSnapshot1.getKey());
                            PayUserModel payUserModel=dataSnapshot1.getValue(PayUserModel.class);
//                            String personName=dataSnapshot1.getKey();
                            String[] separated = payUserModel.getName().split(":_:");
                            payUserModel.setName(separated[0]);
                            list.add(payUserModel);
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
        }catch (Exception c){
            c.printStackTrace();
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<PayUserModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView name,phoneNumber,transectionId,paymentId;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                name=view.findViewById(R.id.student_name);
                phoneNumber=view.findViewById(R.id.phone_number);
                transectionId=view.findViewById(R.id.transection_id);
                paymentId=view.findViewById(R.id.payment_id);
            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<PayUserModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.student_item, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText("Name : "+data.get(position).getName());
            holder.phoneNumber.setText("Phone : "+data.get(position).getPhoneNumber());
            holder.transectionId.setText("Transaction id : "+data.get(position).getTransectionid());
            holder.paymentId.setText("Payment id : "+data.get(position).getPaymentID());


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
