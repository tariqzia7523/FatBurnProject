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

public class AdminVeneuClassDetailActivity extends AppCompatActivity {
    VeneuModel veneuModel;
    DatabaseReference myRefBooked;
    RecyclerView recyclerView;
    TextView title,timming,days,titleText,seats,seatsText;
    ArrayList<VeneuModel> list;
    MyAdapter myAdapter;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_online_class_detail);
        getSupportActionBar().hide();
        TAG="***veneuDet";
        recyclerView=findViewById(R.id.student_list);
        list=new ArrayList<>();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminVeneuClassDetailActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),AdminVeneuClassDetailActivity.this,list);
        recyclerView.setAdapter(myAdapter);
        title=findViewById(R.id.tile);
        seats=findViewById(R.id.seats);
        titleText=findViewById(R.id.title_text);
        titleText.setText("Venue");
        timming=findViewById(R.id.class_timing);
        days=findViewById(R.id.class_days);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try{
            veneuModel=(VeneuModel) getIntent().getExtras().get("data");
            myRefBooked= FirebaseDatabase.getInstance().getReference().child("Classes").child("Booked").child("VeneuClasses").child(veneuModel.getVenue());
            title.setText(veneuModel.getVenue());
            timming.setText(veneuModel.getTime());
            days.setText(veneuModel.getDay());
            myRefBooked.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Log.e(TAG,"datashnap shot 1 id"+dataSnapshot1.getKey());
                            VeneuModel vm=dataSnapshot1.getValue(VeneuModel.class);
//                            String[] separated = dataSnapshot1.getKey().split(":_:");
                            String[] separated = vm.getPersonName().split(":_:");
                            vm.setPersonName(separated[0]);
                            list.add(vm);

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
            seats.setText(veneuModel.getTotalSeats());
        }catch (Exception c){
            c.printStackTrace();
        }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<VeneuModel> data;
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
                time=view.findViewById(R.id.time);
                phoneNumber=view.findViewById(R.id.phone_number);
                transectinid=view.findViewById(R.id.transection_id);
                paymentId=view.findViewById(R.id.payment_id);
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
                    .inflate(R.layout.veneu_student_model, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText("Name : "+data.get(position).getPersonName());
            holder.day.setText("Day : "+data.get(position).getDay());
            holder.time.setText("Time : "+data.get(position).getTime());
            holder.phoneNumber.setText("Phone : "+data.get(position).getPhoneNumber());
            holder.transectinid.setText("Transaction id : "+data.get(position).getTransectionId());
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
