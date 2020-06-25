package com.fvt.fatburnproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OnlineClassesFragment extends Fragment {

    public static OnlineClassesFragment instance;
    ArrayList<OnlineClassModel> list;
    DatabaseReference myRefOnline;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        instance=this;
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
        list=new ArrayList<>();
        recyclerView=root.findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter=new MyAdapter(getContext(),getActivity(),list);
        recyclerView.setAdapter(myAdapter);
        myRefOnline = FirebaseDatabase.getInstance().getReference("Classes").child("OnlineClasses");
        myRefOnline.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             list.clear();
                try {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        OnlineClassModel onlineClassModel=dataSnapshot1.getValue(OnlineClassModel.class);
                        onlineClassModel.setId(dataSnapshot1.getKey());
                        list.add(onlineClassModel);
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
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("***mainfrag","past frag on start");
    }

    public ArrayList<OnlineClassModel> getList() {
        return list;
    }

    public void setList(ArrayList<OnlineClassModel> list) {
        this.list = list;
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<OnlineClassModel> data;
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
                day=view.findViewById(R.id.day);


            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<OnlineClassModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.class_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText(list.get(position).getTitle());
            holder.day.setText(list.get(position).getPrice());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity,AdminOnlineClassDetailActivity.class).putExtra("data",list.get(position)));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    aleartDialog(list.get(position));
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



    public void aleartDialog(OnlineClassModel onlineClassModel){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Are you sure to delete this class?");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               myRefOnline.child(onlineClassModel.getId()).removeValue();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
