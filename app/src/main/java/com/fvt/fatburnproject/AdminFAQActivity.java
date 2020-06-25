package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class AdminFAQActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    ArrayList<FAQModel> faqModels;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_f_a_q);
        getSupportActionBar().hide();
        recyclerView=findViewById(R.id.faq_list);
        faqModels=new ArrayList<>();
        recyclerView=findViewById(R.id.faq_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminFAQActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),AdminFAQActivity.this,faqModels);
        recyclerView.setAdapter(myAdapter);
        progressDialog=new ProgressDialog(AdminFAQActivity.this);
        progressDialog.setMessage("Please wait...");
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("FAQ");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    faqModels.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        FAQModel faqModel=dataSnapshot1.getValue(FAQModel.class);
                        faqModel.setId(dataSnapshot1.getKey());
                        faqModels.add(faqModel);
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
        findViewById(R.id.add_faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminFAQActivity.this, AdminFAQActivityDetail.class));
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<FAQModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            TextView question,answer;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                question=view.findViewById(R.id.question);
                answer=view.findViewById(R.id.answer);
                answer.setVisibility(View.GONE);



            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<FAQModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.faq_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.question.setText(data.get(position).getQuestion());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, AdminFAQActivityDetail.class);
                    intent.putExtra("data",data.get(position));
                    intent.putExtra("type","admin");
                    activity.startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myRef.child(data.get(position).getId()).removeValue();
                    return false;
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
